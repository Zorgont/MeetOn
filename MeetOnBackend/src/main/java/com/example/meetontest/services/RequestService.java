package com.example.meetontest.services;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.Request;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.entities.User;
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