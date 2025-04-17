package com.example.lab4.models.dtos;

public class DeleteRoomDTO {
    private Long id;

    public DeleteRoomDTO() {
    }

    public DeleteRoomDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
