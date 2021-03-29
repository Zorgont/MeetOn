package com.example.meetontest.services;

import com.example.meetontest.entities.User;

public interface UserService {
    Iterable<User> getUsers();
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(Long id, User newUser);
    void deleteUser(Long id);
    User getUserByName(String name);
}