package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Request;
import com.example.meetontest.api.entities.User;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    Request create(Request request);
    Optional<Request> getById(Long id);
    List<Request> getByUser(User user);
    List<Request> getByMeeting(Meeting meeting);
}
