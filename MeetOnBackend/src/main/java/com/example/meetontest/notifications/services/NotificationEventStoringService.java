package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.entities.NotificationEvent;

import java.util.List;

public interface NotificationEventStoringService {
    void saveEvent(NotificationEvent event);

    List<NotificationEvent> getUnsentEventsList();

    void updateEvent(NotificationEvent event);

}
