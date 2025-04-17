package com.example.lab4.models;

import com.example.lab4.annotations.Cacheable;
import com.example.lab4.annotations.DistributedLock;
import com.example.lab4.redis.RedisClient;
import com.example.lab4.models.dtos.CreateRoomDTO;
import com.example.lab4.models.dtos.UpdateRoomDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired  
    private RedisClient redisClient;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String ALL_ROOMS_CACHE_KEY = "rooms:all";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final String ROOM_LOCK_PREFIX = "room";

    public List<Room> getAllRooms() {
        try {
            String cachedRoomsJson = redisClient.get(ALL_ROOMS_CACHE_KEY);
            if (cachedRoomsJson != null) {
                log.info("Cache hit for key: {}", ALL_ROOMS_CACHE_KEY);
                return objectMapper.readValue(cachedRoomsJson, new TypeReference<List<Room>>() {});
            }
        } catch (JsonProcessingException e) {
            log.error("Error deserializing cached rooms: {}", e.getMessage());
        }

        log.info("Cache miss for key: {}. Fetching from database.", ALL_ROOMS_CACHE_KEY);
        List<Room> rooms = roomRepository.findAll();
        cacheRooms(rooms);
        return rooms;
    }

    private void cacheRooms(List<Room> rooms) {
        try {
            String roomsJson = objectMapper.writeValueAsString(rooms);
            redisClient.set(ALL_ROOMS_CACHE_KEY, roomsJson, CACHE_TTL);
            log.info("Updated cache for key: {}", ALL_ROOMS_CACHE_KEY);
        } catch (JsonProcessingException e) {
            log.error("Error serializing rooms for cache: {}", e.getMessage());
        }
    }

    private void invalidateCache() {
        redisClient.delete(ALL_ROOMS_CACHE_KEY);
        log.info("Invalidated cache for key: {}", ALL_ROOMS_CACHE_KEY);
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Optional<Room> getRoomByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    @Transactional
    public Room createRoom(CreateRoomDTO roomDTO) {
        if (roomRepository.findByRoomNumber(roomDTO.getRoomNumber()).isPresent()) {
            throw new IllegalArgumentException("Room number " + roomDTO.getRoomNumber() + " already exists.");
        }

        Room room = new Room(
            roomDTO.getRoomNumber(),
            roomDTO.getType(),
            roomDTO.getPrice()  // Assuming CreateRoomDTO.getPrice() already returns BigDecimal
        );
        
        Room savedRoom = roomRepository.save(room);
        invalidateCache(); // Invalidate cache after creating a new room
        return savedRoom;
    }

    @Transactional
    @DistributedLock(keyPrefix = ROOM_LOCK_PREFIX, keyIdentifierExpression = "#id", leaseTime = 120, timeUnit = TimeUnit.SECONDS)
    public Room updateRoom(Long id, UpdateRoomDTO roomDTO) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));

        if (roomDTO.getRoomNumber() != null && !roomDTO.getRoomNumber().equals(existingRoom.getRoomNumber())) {
            if (roomRepository.findByRoomNumber(roomDTO.getRoomNumber()).isPresent()) {
                throw new IllegalArgumentException("Room number " + roomDTO.getRoomNumber() + " already exists.");
            }
        }

        BeanUtils.copyProperties(roomDTO, existingRoom, getNullPropertyNames(roomDTO));
        Room updatedRoom = roomRepository.save(existingRoom);
        invalidateCache(); // Invalidate cache after updating a room
        return updatedRoom;
    }

    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
        roomRepository.delete(room);
        invalidateCache(); // Invalidate cache after deleting a room
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Transactional
    @DistributedLock(keyPrefix = ROOM_LOCK_PREFIX, keyIdentifierExpression = "#id", leaseTime = 120, timeUnit = TimeUnit.SECONDS)
    public Room performLongRunningOperation(Long id) throws InterruptedException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
        
        log.info("Starting long-running operation for room {}", id);
        log.info("Simulating a long operation - sleeping for 10 seconds...");
        Thread.sleep(10000); // 10 seconds delay
        log.info("Completed long-running operation for room {}", id);
        
        return room;
    }

    @Cacheable(ttl = 300, timeUnit = TimeUnit.SECONDS)  // Cache for 5 minutes
    public Room getRoomByIdWithCache(Long id) {
        log.info("Fetching room with ID {} from database", id);
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }
} 