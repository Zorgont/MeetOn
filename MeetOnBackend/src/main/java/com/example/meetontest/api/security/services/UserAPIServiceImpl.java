package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.exceptions.ResourceNotFoundException;
import com.example.meetontest.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAPIServiceImpl implements UserAPIService{

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
