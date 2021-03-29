package com.example.meetontest.services;

import com.example.meetontest.dto.LoginRequest;
import com.example.meetontest.dto.SignupRequest;
import com.example.meetontest.dto.JwtResponse;

public interface AuthService {
     JwtResponse authenticateUser(LoginRequest loginRequest);
     void registerUser(SignupRequest signUpRequest);
}