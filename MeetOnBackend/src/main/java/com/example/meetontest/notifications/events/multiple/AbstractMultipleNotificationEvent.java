package com.example.meetontest.notifications.events.multiple;

import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.events.AbstractNotificationEvent;

import java.util.List;
import java.util.Map;

public interface AbstractMultipleNotificationEvent<D, T> extends AbstractNotificationEvent<T> {
    Map<D, List<NotificationEvent>> preprocess(List<NotificationEvent> events);
    void process(D key, List<NotificationEvent> events);
}
