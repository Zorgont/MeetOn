package com.example.meetontest.services.impl;

import com.example.meetontest.dto.JwtResponse;
import com.example.meetontest.dto.LoginRequest;
import com.example.meetontest.dto.SignupRequest;
import com.example.meetontest.entities.ConfirmationToken;
import com.example.meetontest.entities.ERole;
import com.example.meetontest.entities.Role;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.repositories.RoleRepository;
import com.example.meetontest.repositories.UserRepository;
import com.example.meetontest.security.JwtUtils;
import com.example.meetontest.services.AuthService;
import com.example.meetontest.services.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final private AuthenticationManager authenticationManager;
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private EmailService emailService;
    final private ConfirmationTokenService confirmationTokenService;
    final private PasswordEncoder encoder;
    final private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(
                userDetails.getId(),
                jwt, userDetails.getUsername(),
                userDetails.getEmail(), roles);
    }

    public void registerUser(SignupRequest signUpRequest) throws ValidatorException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ValidatorException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ValidatorException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(), false);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new ValidatorException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        ConfirmationToken token = confirmationTokenService.createToken(user);
        try {
            emailService.sendConfirmationMessage(user, token.getConfirmationToken());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Transactional
    public void confirmUser(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getByConfirmationToken(token);
        if(confirmationToken != null){
            User user = userRepository.findByUsername(confirmationToken.getUser().getUsername()).get();

            user.setIsEnabled(true);
            userRepository.save(user);
            confirmationTokenService.deleteByConfirmationToken(token);
        }

        else throw new ValidatorException("Confirmation error!");
    }
}