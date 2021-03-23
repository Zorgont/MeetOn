package com.example.meetontest.api.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.meetontest.api.entities.ERole;
import com.example.meetontest.api.entities.Role;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.payload.request.LoginRequest;
import com.example.meetontest.api.payload.request.SignupRequest;
import com.example.meetontest.api.payload.response.JwtResponse;
import com.example.meetontest.api.payload.response.MessageResponse;
import com.example.meetontest.api.repositories.RoleRepository;
import com.example.meetontest.api.repositories.UserRepository;
import com.example.meetontest.api.security.jwt.JwtUtils;
import com.example.meetontest.api.security.services.AuthService;
import com.example.meetontest.api.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }
}