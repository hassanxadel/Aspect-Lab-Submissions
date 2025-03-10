package com.example.lab2.User;

import com.example.lab2.User.dto.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return this.userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody CreateUserDto createUserDto) {
        return this.userService.createUser(createUserDto);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody CreateUserDto createUserDto) {
        return this.userService.updateUser(id, createUserDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        this.userService.deleteUser(id);
    }
}
