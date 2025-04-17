package com.example.lab4.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private String type; // e.g., Single, Double, Suite

    @Column(nullable = false)
    private BigDecimal price; // Price per night

    @Column(nullable = false)
    private boolean isReserved = false; // Default to not reserved

    // Constructors
    public Room() {
    }

    public Room(String roomNumber, String type, BigDecimal price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isReserved = false; // Ensure default state
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    @Override
    public String toString() {
        return "Room{" +
               "id=" + id +
               ", roomNumber='" + roomNumber + '\'' +
               ", type='" + type + '\'' +
               ", price=" + price +
               ", isReserved=" + isReserved +
               '}';
    }
} 