package com.example.meetontest.services.impl;

import com.example.meetontest.dto.UserSettingDTO;
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

    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
    }

    @Override
    public User updateUser(Long id, User newUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
        user.setFirstName(newUser.getFirstName());
        user.setAbout(newUser.getAbout());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!")));
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));
    }

    @Override
    public UserSettingDTO updateUserSettings(Long id, UserSettingDTO userSettingDTO) {
        User entity = userRepository.findById(id).get();
        entity.setFirstName(userSettingDTO.getFirstName());
        entity.setSecondName(userSettingDTO.getSecondName());
        entity.setAbout(userSettingDTO.getAbout());
        userRepository.save(entity);
        return userSettingDTO;
    }
}