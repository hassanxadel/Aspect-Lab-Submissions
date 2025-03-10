package com.example.lab3Final.Readers.dto;

public class CreateReaderDTO {
    private String name;
    private String email;

    public CreateReaderDTO() {}

    public CreateReaderDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
