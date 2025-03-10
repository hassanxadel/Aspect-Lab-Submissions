package com.example.demo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Before("execution(* com.example.demo.services.MyService.*(..))")
    public void beforeMyServiceMethods() {
        System.out.println("Aspect: Before MyService methods");
    }
}
