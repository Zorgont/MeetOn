package com.example.meetontest.api.controllers;

import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.security.services.UserAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserAPIService userAPIService;
    @GetMapping
    public Iterable<User> getUsers() {
        return  userAPIService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return  userAPIService.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userAPIService.getUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return userAPIService.updateUser(id,newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        return userAPIService.deleteUser(id);
    }
}
