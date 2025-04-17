package com.example.lab4.models.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class UpdateRoomDTO {

    @Size(min = 1, max = 10, message = "Room number must be between 1 and 10 characters")
    private String roomNumber;

    @Size(min = 3, max = 50, message = "Room type must be between 3 and 50 characters")
    private String type;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    private Boolean isReserved; // Allow updating reservation status

    // Default constructor
    public UpdateRoomDTO() {
    }

    // Constructor with fields
    public UpdateRoomDTO(String roomNumber, String type, BigDecimal price, Boolean isReserved) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isReserved = isReserved;
    }

    // Getters and Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getReserved() {
        return isReserved;
    }

    public void setReserved(Boolean reserved) {
        isReserved = reserved;
    }
} 