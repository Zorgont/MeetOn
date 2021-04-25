package com.example.meetontest.notifications.services;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.*;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationEventStatus;
import com.example.meetontest.notifications.events.AbstractNotificationEvent;
import com.example.meetontest.notifications.events.RequestCreatedEvent;
import com.example.meetontest.services.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationSendingService {
    private static final Logger log = LoggerFactory.getLogger(NotificationSendingService.class);

    private final NotificationEventStoringService notificationEventStoringService;
    private final MeetingService meetingService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Autowired
    @Qualifier("notificationEventMap")
    Map<String, AbstractNotificationEvent> notificationEventMap;

    @Scheduled(fixedRate = 10000)
    public void checkEvents() {
        notificationEventStoringService.getUnsentEventsList().forEach(notificationEvent -> {
            try {
                notificationEventMap.get(notificationEvent.getType()).process(notificationEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // once per 1 min.
    @Scheduled(fixedRate = 60000)
    public void checkUnsentRequestCreatedEvents() {
        notificationEventStoringService.getUnsentEventsList().stream().filter(event -> event.getType().equals(RequestCreatedEvent.class.getSimpleName()))
                .collect(Collectors.groupingBy(event -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, RequestDTO> map = objectMapper.readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {
                        });

                        return meetingService.getMeetingById(map.get("new").getMeeting_id()).getId();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }))
                .forEach((meetingId, events) -> {
                    Meeting meeting = meetingService.getMeetingById(meetingId);
                    emailService.sendSimpleMessage(meetingService.getManager(meeting).getEmail(), "New requests for meeting " + meeting.getName(), "You have new " + events.size() + " requests on meeting " + meeting.getName());
                    events.forEach(event -> {
                        event.setStatus(NotificationEventStatus.SENT);
                        notificationEventStoringService.updateEvent(event);
                    });
                    notificationService.createNotification(new Notification(new Date(), events.size() + " new requests on meeting " + meeting.getName(), meetingService.getManager(meeting)));
                });
    }
}
