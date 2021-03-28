package com.example.meetontest.api.controllers;

import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.payload.response.MessageResponse;
import com.example.meetontest.api.security.services.UserAPIService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userAPIService.getUserById(id));
        }
        catch (Exception e){
             return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        try {
            return ResponseEntity.ok(userAPIService.updateUser(id,newUser));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userAPIService.deleteUser(id);
        return ResponseEntity.ok("User deleted!");
    }
}
