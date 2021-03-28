package com.example.meetontest.api.security.services;

import com.example.meetontest.api.payload.request.LoginRequest;
import com.example.meetontest.api.payload.request.SignupRequest;
import com.example.meetontest.api.payload.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
     JwtResponse authenticateUser(LoginRequest loginRequest);
     void registerUser(SignupRequest signUpRequest);
}
