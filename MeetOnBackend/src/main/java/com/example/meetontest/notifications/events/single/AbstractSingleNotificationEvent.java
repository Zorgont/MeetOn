package com.example.meetontest.notifications.events.single;

import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.events.AbstractNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;

public interface AbstractSingleNotificationEvent<T> extends AbstractNotificationEvent<T> {
    void process(NotificationEvent event) throws JsonProcessingException, ParseException;
}
