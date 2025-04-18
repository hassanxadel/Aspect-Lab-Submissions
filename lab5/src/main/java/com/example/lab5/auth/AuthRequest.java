package com.example.lab5.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}