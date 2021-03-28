package com.example.meetontest.api.services;

import com.example.meetontest.api.entities.User;

public interface UserAPIService {
    Iterable<User> getUsers();
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(Long id, User newUser);
    void deleteUser(Long id);
    User getUserByName(String name);
}
