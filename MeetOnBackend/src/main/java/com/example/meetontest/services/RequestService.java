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
    Optional<Request> getByMeetingIdUserId(Long meetingId, Long userId);
    void removeById(Long id);
    int getApprovedRequestsAmount(long meetingId);
    List<Request> getByMeetingAndStatus(Meeting meeting,RequestStatus status);
}