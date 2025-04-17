package com.example.lab4.aspects;

import com.example.lab4.annotations.RateLimit;
import com.example.lab4.Exceptions.RateLimitExceededException;
import com.example.lab4.redis.RedisClient;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
public class RateLimitingAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingAspect.class);

    @Autowired
    private RedisClient redisClient;

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        log.info("Rate limiting aspect triggered for method: {}", joinPoint.getSignature().getName());

        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) {
            log.warn("Rate limiting skipped: Could not obtain HttpServletRequest.");
            return joinPoint.proceed();
        }

        String clientIp = getClientIp(request);
        String methodKey = getMethodKey(joinPoint);
        String rateLimitKey = buildRateLimitKey(rateLimit.keyPrefix(), methodKey, clientIp);
        
        log.info("Rate limit check - Client IP: {}, Method: {}, Key: {}", clientIp, methodKey, rateLimitKey);

        long limit = rateLimit.limit();
        Duration windowDuration = Duration.of(rateLimit.duration(), rateLimit.timeUnit().toChronoUnit());
        
        log.info("Rate limit configuration - Limit: {}, Duration: {} {}", 
                limit, rateLimit.duration(), rateLimit.timeUnit());

        // 1. Increment the counter for the key
        Long currentCount = redisClient.increment(rateLimitKey);
        log.info("Current request count: {}", currentCount);

        if (currentCount == null) {
            log.error("Failed to increment rate limit counter for key: {}", rateLimitKey);
            return joinPoint.proceed();
        }

        // 2. If it's the first request in this window, set the expiration
        if (currentCount == 1) {
            boolean expirationSet = redisClient.expire(rateLimitKey, windowDuration);
            log.info("First request in window. Set expiration: {} for duration: {}", expirationSet, windowDuration);
        }

        // 3. Check if the limit is exceeded
        if (currentCount > limit) {
            log.warn("Rate limit exceeded - Key: {}, Count: {}, Limit: {}", rateLimitKey, currentCount, limit);
            throw new RateLimitExceededException(
                    String.format("Rate limit exceeded. Limit: %d requests per %d %s. Current count: %d",
                            limit, rateLimit.duration(), rateLimit.timeUnit().name().toLowerCase(), currentCount)
            );
        }

        log.info("Rate limit check passed - Key: {}, Count: {}, Limit: {}", rateLimitKey, currentCount, limit);
        return joinPoint.proceed();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getRequest() : null;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0].trim();
    }

    private String getMethodKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }

    private String buildRateLimitKey(String prefix, String methodKey, String clientIp) {
        if (prefix != null && !prefix.trim().isEmpty()) {
             return "rate_limit:" + prefix.trim() + ":" + clientIp;
        } else {
             return "rate_limit:" + methodKey + ":" + clientIp;
        }
    }
}