package com.example.lab4.aspects;

import com.example.lab4.annotations.Cacheable;
import com.example.lab4.redis.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
public class CacheAspect {

    private static final Logger log = LoggerFactory.getLogger(CacheAspect.class);
    
    @Autowired
    private RedisClient redisClient;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation(cacheable)")
    public Object cache(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // Generate cache key based on method name and parameters
        String cacheKey = generateCacheKey(method, joinPoint.getArgs());
        
        // Try to get from cache first
        try {
            String cachedValue = redisClient.get(cacheKey);
            if (cachedValue != null) {
                log.info("Cache hit for key: {}", cacheKey);
                return objectMapper.readValue(cachedValue, objectMapper.constructType(method.getGenericReturnType()));
            }
        } catch (JsonProcessingException e) {
            log.error("Error deserializing cached value for key {}: {}", cacheKey, e.getMessage());
        }
        
        // If not in cache, execute method and cache result
        log.info("Cache miss for key: {}", cacheKey);
        Object result = joinPoint.proceed();
        
        if (result != null) {
            try {
                String jsonValue = objectMapper.writeValueAsString(result);
                redisClient.set(cacheKey, jsonValue, Duration.of(cacheable.ttl(), cacheable.timeUnit().toChronoUnit()));
                log.info("Cached result for key: {}", cacheKey);
            } catch (JsonProcessingException e) {
                log.error("Error serializing value for caching for key {}: {}", cacheKey, e.getMessage());
            }
        }
        
        return result;
    }
    
    private String generateCacheKey(Method method, Object[] args) {
        StringBuilder key = new StringBuilder("cache:")
            .append(method.getDeclaringClass().getSimpleName())
            .append(":")
            .append(method.getName());
            
        if (args != null && args.length > 0) {
            key.append(":");
            for (Object arg : args) {
                key.append(arg.toString()).append("_");
            }
        }
        
        return key.toString();
    }
}
