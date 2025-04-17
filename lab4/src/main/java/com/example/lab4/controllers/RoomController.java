package com.example.lab4.controllers;

import com.example.lab4.models.Room;
import com.example.lab4.models.RoomService;
import com.example.lab4.models.dtos.CreateRoomDTO;
import com.example.lab4.models.dtos.UpdateRoomDTO;
import com.example.lab4.annotations.RateLimit;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @GetMapping
    @RateLimit(limit = 5, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<List<Room>> getAllRooms() throws Exception {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    @RateLimit(limit = 5, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @RateLimit(limit = 3, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Room> createRoom(@RequestBody CreateRoomDTO roomDTO) {
        Room createdRoom = roomService.createRoom(roomDTO);
        return ResponseEntity.ok(createdRoom);
    }

    @PutMapping("/{id}")
    @RateLimit(limit = 2, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody UpdateRoomDTO roomDTO) {
        try {
            Room updatedRoom = roomService.updateRoom(id, roomDTO);
            return ResponseEntity.ok(updatedRoom);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @RateLimit(limit = 2, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/operation")
    @RateLimit(limit = 2, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Room> performOperation(@PathVariable Long id) throws InterruptedException {
        return ResponseEntity.ok(roomService.performLongRunningOperation(id));
    }
    
    @GetMapping("/{id}/operation-concurrent")
    @RateLimit(limit = 2, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<String> performConcurrentOperations(@PathVariable Long id) {
        CompletableFuture<Room> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                return roomService.performLongRunningOperation(id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
        
        CompletableFuture<Room> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                return roomService.performLongRunningOperation(id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
        
        CompletableFuture.allOf(future1, future2).join();
        
        return ResponseEntity.ok("Concurrent operations completed");
    }

    @GetMapping("/{id}/cached")
    @RateLimit(limit = 10, duration = 60, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Room> getRoomByIdCached(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomByIdWithCache(id);
            return ResponseEntity.ok(room);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 