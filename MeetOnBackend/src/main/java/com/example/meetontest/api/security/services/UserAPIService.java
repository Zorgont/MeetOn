package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserAPIService {
    Iterable<User> getUsers();
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(Long id, User newUser);
    void deleteUser(Long id);
    User getUserByName(String name);
}
