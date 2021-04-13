package com.example.meetontest.services.impl;

import com.example.meetontest.converters.RequestConverter;
import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.Request;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.entities.User;
import com.example.meetontest.notifications.events.RequestStatusChangedEvent;
import com.example.meetontest.notifications.services.NotificationEventStoringService;
import com.example.meetontest.repositories.RequestRepository;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final MeetingService meetingService;
    private final UserService userService;
    private final NotificationEventStoringService notificationEventStoringService;
    private final RequestConverter requestConverter;

    @Override
    public Request create(Request request) {
        if (request.getMeeting().getIsPrivate())
            request.setStatus(RequestStatus.PENDING);
        else if (!request.getMeeting().getIsPrivate())
            request.setStatus(RequestStatus.APPROVED);

        return requestRepository.save(request);
    }

    @Override
    public Optional<Request> getById(Long id) {
        return requestRepository.findById(id);
    }

    @Override
    public List<Request> getByUser(User user) {
        return requestRepository.findByUser(user);
    }

    @Override
    public List<Request> getByMeeting(Meeting meeting) {
        return requestRepository.findByMeeting(meeting);
    }

    @Override
    public void changeStatus(Request request, RequestStatus status) {
        RequestDTO oldRequest = requestConverter.convertBack(request);
        request.setStatus(status);
        requestRepository.save(request);
        notificationEventStoringService.saveEvent(new RequestStatusChangedEvent(new Date(), oldRequest, requestConverter.convertBack(request)));
    }

    @Override
    public Optional<Request> getByMeetingIdUserId(Long meetingId, Long userId) {
        return requestRepository.findByMeetingAndUser(meetingService.getMeetingById(meetingId), userService.getUserById(userId));
    }

    @Override
    public void removeById(Long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public int getApprovedRequestsAmount(long meetingId) {
        return requestRepository.countByMeetingAndStatus(meetingService.getMeetingById(meetingId), RequestStatus.APPROVED);
    }

    @Override
    public List<Request> getByMeetingAndStatus(Meeting meeting, RequestStatus status) {
        return requestRepository.findByMeetingAndStatus(meeting,status);
    }
}