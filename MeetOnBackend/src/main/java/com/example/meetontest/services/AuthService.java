package com.example.meetontest.services;

import com.example.meetontest.dto.JwtResponse;
import com.example.meetontest.dto.LoginRequest;
import com.example.meetontest.dto.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    public void confirmUser(String token);
    void registerUser(SignupRequest signUpRequest);
}