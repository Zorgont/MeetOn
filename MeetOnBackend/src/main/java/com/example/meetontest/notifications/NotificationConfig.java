package com.example.meetontest.notifications;

import com.example.meetontest.notifications.events.multiple.AbstractMultipleNotificationEvent;
import com.example.meetontest.notifications.events.single.impl.MeetingChangedEvent;
import com.example.meetontest.notifications.events.multiple.impl.RequestCreatedEvent;
import com.example.meetontest.notifications.events.single.impl.RequestStatusChangedEvent;
import com.example.meetontest.notifications.events.single.AbstractSingleNotificationEvent;
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
    public HashMap<String, AbstractSingleNotificationEvent> notificationSingleEventMap() {
        HashMap<String, AbstractSingleNotificationEvent> result = new HashMap<>();

        result.put("MeetingChangedEvent", meetingChangedEvent);
        result.put("RequestStatusChangedEvent", requestStatusChangedEvent);
        return result;
    }

    @Bean
    public HashMap<String, AbstractMultipleNotificationEvent> notificationMultipleEventMap() {
        HashMap<String, AbstractMultipleNotificationEvent> result = new HashMap<>();
        result.put("RequestCreatedEvent", requestCreatedEvent);
        return result;
    }
}
