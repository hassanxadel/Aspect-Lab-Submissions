package com.example.Install.AOP.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    
    @Before("execution(* com.example.Install.AOP.service.*.*(..))")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        System.out.println("Aspect: Before executing method - " + joinPoint.getSignature().getName());
    }
} 