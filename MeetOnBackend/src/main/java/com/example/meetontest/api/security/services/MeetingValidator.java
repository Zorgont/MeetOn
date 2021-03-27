package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import com.example.meetontest.api.repositories.TagRepository;
import com.example.meetontest.api.repositories.UserRepository;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

public interface MeetingValidator {
    public ResponseEntity<?> validate (CreateMeetingRequest meetingRequest, Set<Tag> tags , Optional<User> manager);
}
