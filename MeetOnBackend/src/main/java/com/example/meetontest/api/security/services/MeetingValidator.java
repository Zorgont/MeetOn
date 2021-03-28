package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

public interface MeetingValidator {
    void validate (Meeting meeting);
}
