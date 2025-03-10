package com.example.demo.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.Services.service;

@RestController
public class Controller {
    private final service srv;

    @Autowired
    public Controller(service srv) {
        this.srv = srv;
    }

    @GetMapping("/hello-world")
    public String sayHello() {
        srv.doSomething();
        return "Hello World";
    }

    @DeleteMapping("/hello-delete")
    public String sayHello2() {
        return "Hello Delete";
    }

    @PostMapping("/hello-post")
    public String sayHello3() {
        return "Hello Post";
    }
    
    @PutMapping("/hello-put")
    public String sayHello4() {
        return "Hello Put";
    }
}