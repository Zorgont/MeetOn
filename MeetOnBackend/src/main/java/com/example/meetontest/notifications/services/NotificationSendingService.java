package com.example.meetontest.notifications.services;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.*;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.entities.NotificationEventStatus;
import com.example.meetontest.notifications.events.MeetingChangedEvent;
import com.example.meetontest.notifications.events.NotificationAbstractEvent;
import com.example.meetontest.notifications.events.RequestCreatedEvent;
import com.example.meetontest.notifications.events.RequestStatusChangedEvent;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final MeetingConverter meetingConverter;
    private final MeetingService meetingService;
    private final RequestService requestService;
    private final UserService userService;

    @Scheduled(fixedRate = 10000)
    public void checkEvents() {
        notificationEventStoringService.getUnsentEventsList().forEach(notificationEvent -> {
            try {
                String className = "com.example.meetontest.notifications.events." + notificationEvent.getType();
                NotificationAbstractEvent event = (NotificationAbstractEvent) Class.forName(className).getConstructor(NotificationEvent.class).newInstance(new Object[] { notificationEvent });
                event.injectServices(userService, meetingService, requestService, meetingConverter, emailService, notificationService, notificationEventStoringService);
                event.process();
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
