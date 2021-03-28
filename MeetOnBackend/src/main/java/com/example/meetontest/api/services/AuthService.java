package com.example.meetontest.api.services;

import com.example.meetontest.api.dto.LoginRequest;
import com.example.meetontest.api.dto.SignupRequest;
import com.example.meetontest.api.dto.JwtResponse;

public interface AuthService {
     JwtResponse authenticateUser(LoginRequest loginRequest);
     void registerUser(SignupRequest signUpRequest);
}
