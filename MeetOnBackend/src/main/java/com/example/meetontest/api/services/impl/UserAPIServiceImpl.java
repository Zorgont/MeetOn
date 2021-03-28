package com.example.meetontest.api.services.impl;

import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.exceptions.ResourceNotFoundException;
import com.example.meetontest.api.repositories.UserRepository;
import com.example.meetontest.api.services.UserAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAPIServiceImpl implements UserAPIService {

    private final UserRepository userRepository;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }


    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
        return user;
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

    @Override
    public User getUserByName(String name) {
        return userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
    }

}
