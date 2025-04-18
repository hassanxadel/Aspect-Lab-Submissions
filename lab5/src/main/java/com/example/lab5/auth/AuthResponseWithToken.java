package com.example.lab5.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseWithToken extends AuthResponse {
    private String token;
    
    public AuthResponseWithToken(String message, String token) {
        super(message);
        this.token = token;
    }
} 