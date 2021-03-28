package com.example.meetontest.api.services;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Request;
import com.example.meetontest.api.entities.RequestStatus;
import com.example.meetontest.api.entities.User;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    Request create(Request request);
    Optional<Request> getById(Long id);
    List<Request> getByUser(User user);
    List<Request> getByMeeting(Meeting meeting);
    void changeStatus(Request request, RequestStatus status);
    boolean existsByMeetingIdUserId(Long meetingId,Long userId);
}
