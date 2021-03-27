package com.example.meetontest.api.security.services;

import com.example.meetontest.api.payload.request.LoginRequest;
import com.example.meetontest.api.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest);
}
