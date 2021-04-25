package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.events.multiple.AbstractMultipleNotificationEvent;
import com.example.meetontest.notifications.events.single.AbstractSingleNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationSendingService {
    private final NotificationEventStoringService notificationEventStoringService;

    @Autowired
    @Qualifier("notificationSingleEventMap")
    Map<String, AbstractSingleNotificationEvent> notificationSingleEventMap;

    @Autowired
    @Qualifier("notificationMultipleEventMap")
    Map<String, AbstractMultipleNotificationEvent> notificationMultipleEventMap;

    @Scheduled(fixedRate = 10000)
    public void checkEvents() {
        notificationEventStoringService.getUnsentEventsList().stream().filter(event -> notificationSingleEventMap.containsKey(event.getType()))
            .forEach(notificationEvent -> {
                try {
                    notificationSingleEventMap.get(notificationEvent.getType()).process(notificationEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
    }

    // once per 1 min.
    @Scheduled(fixedRate = 60000)
    public void checkUnsentRequestCreatedEvents() {
        notificationEventStoringService.getUnsentEventsList().stream().filter(event -> notificationMultipleEventMap.containsKey(event.getType()))
            .collect(Collectors.groupingBy(NotificationEvent::getType)).forEach((eventType, events) -> {
                AbstractMultipleNotificationEvent event = notificationMultipleEventMap.get(eventType);
                event.preprocess(events).forEach((meetingId, eventList) -> event.process(meetingId, (List<NotificationEvent>) eventList));
            });
    }
}
