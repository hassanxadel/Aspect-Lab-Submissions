package com.example.demo.Aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Aspects {

    @Before("execution(* com.example.demo.Services.service.*(..))")
    public void BeforeServices() {
        System.out.println("Aspect : A method in Servise is being called");
    }
}