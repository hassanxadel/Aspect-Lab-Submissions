package com.example.lab3Final.Readers.dto;

public class DeleteReaderDTO {
    private Long id;

    public DeleteReaderDTO() {}

    public DeleteReaderDTO(Long id) {
        this.id = id;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
