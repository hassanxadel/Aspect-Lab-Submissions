package com.example.demo.Aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Aspects {

    @Before("execution(* com.example.demo.Services.service.doSomething(..))")
    public void BeforeServices() {
        System.out.println("Aspect : Method is called");
    }
}