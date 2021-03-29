package com.example.meetontest.services.impl;

import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ResourceNotFoundException;
import com.example.meetontest.repositories.UserRepository;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
    }

    public User updateUser(Long id, User newUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
        user.setFirstName(newUser.getFirstName());
        user.setAbout(newUser.getAbout());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!")));
    }

    public User getUserByName(String name) {
        return userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
    }
}