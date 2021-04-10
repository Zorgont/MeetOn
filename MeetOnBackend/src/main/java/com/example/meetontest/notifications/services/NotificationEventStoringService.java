package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.events.NotificationAbstractEvent;

import java.util.List;

public interface NotificationEventStoringService {
    void saveEvent(NotificationAbstractEvent event);
    List<NotificationEvent> getUnsentEventsList();
    void updateEvent(NotificationEvent event);
}
