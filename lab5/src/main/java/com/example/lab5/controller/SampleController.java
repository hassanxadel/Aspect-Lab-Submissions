package com.example.lab5.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SampleController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint that anyone can access.";
    }

    @GetMapping("/protected")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String protectedEndpoint() {
        return "Access granted to protected endpoint. You are authenticated as ADMIN!";
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminEndpoint() {
        return "Access granted to admin endpoint. You have ADMIN role!";
    }
    
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public String userEndpoint() {
        return "Access granted to user endpoint. You have USER role!";
    }
    
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profileEndpoint(Authentication authentication) {
        return "Hello " + authentication.getName() + "! This is your profile.";
    }
}
