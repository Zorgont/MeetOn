package com.example.meetontest.notifications;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.events.AbstractNotificationEvent;
import com.example.meetontest.notifications.events.MeetingChangedEvent;
import com.example.meetontest.notifications.events.RequestCreatedEvent;
import com.example.meetontest.notifications.events.RequestStatusChangedEvent;
import com.example.meetontest.notifications.services.NotificationEventStoringService;
import com.example.meetontest.notifications.services.NotificationService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class NotificationConfig {
    private final MeetingChangedEvent meetingChangedEvent;
    private final RequestCreatedEvent requestCreatedEvent;
    private final RequestStatusChangedEvent requestStatusChangedEvent;

    @Bean
    public HashMap<String, AbstractNotificationEvent> notificationEventMap() {
        HashMap<String, AbstractNotificationEvent> result = new HashMap<>();

        result.put("MeetingChangedEvent", meetingChangedEvent);
        result.put("RequestCreatedEvent", requestCreatedEvent);
        result.put("RequestStatusChangedEvent", requestStatusChangedEvent);
        return result;
    }
}
