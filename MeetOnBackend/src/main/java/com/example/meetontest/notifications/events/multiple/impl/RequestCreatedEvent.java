package com.example.meetontest.notifications.events.multiple.impl;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.entities.NotificationEventStatus;
import com.example.meetontest.notifications.events.multiple.AbstractMultipleNotificationEvent;
import com.example.meetontest.notifications.services.NotificationEventStoringService;
import com.example.meetontest.notifications.services.NotificationService;
import com.example.meetontest.services.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestCreatedEvent implements AbstractMultipleNotificationEvent<Long, RequestDTO> {
    private final MeetingService meetingService;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final NotificationEventStoringService notificationEventStoringService;

    @Override
    public Map<Long, List<NotificationEvent>> preprocess(List<NotificationEvent> events) {
        return events.stream().collect(Collectors.groupingBy(event -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, RequestDTO> map = objectMapper.readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {
                });

                return meetingService.getMeetingById(map.get("new").getMeeting_id()).getId();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }));
    }

    @Override
    public void process(Long meetingId, List<NotificationEvent> events) {
        Meeting meeting = meetingService.getMeetingById(meetingId);
        emailService.sendSimpleMessage(meetingService.getManager(meeting).getEmail(), "New requests for meeting " + meeting.getName(), "You have new " + events.size() + " requests on meeting " + meeting.getName());
        events.forEach(event -> {
            event.setStatus(NotificationEventStatus.SENT);
            notificationEventStoringService.updateEvent(event);
        });
        notificationService.createNotification(new Notification(new Date(), events.size() + " new requests on meeting " + meeting.getName(), meetingService.getManager(meeting)));
    }
}
