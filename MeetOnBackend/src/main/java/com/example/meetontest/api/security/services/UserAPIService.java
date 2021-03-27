package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserAPIService {
    public Iterable<User> getUsers();
    public User createUser(User user);
    public ResponseEntity<User> getUserById(Long id);
    public ResponseEntity<User> updateUser(Long id, User newUser);
    public ResponseEntity<Map<String, Boolean>> deleteUser(Long id);
    public User getUserByName(String name);
}
