package com.example.demo.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


@GetMapping("/hello world")
public String sayHello(){
    return "Hello World";
}

@DeleteMapping("/hello delete")
    public String sayHello2(){
    return "Hello Delete";
    }

@PostMapping("/hello post")
    public String sayHello3(){
        return "Hello Post";
    }
    
@PutMapping("/hello put1")
    public String sayHello4(){
        return "Hello Put";
    }
}