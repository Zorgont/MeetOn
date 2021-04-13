package com.example.meetontest.notifications.events;

import com.example.meetontest.notifications.entities.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class NotificationAbstractEvent<T> {
    protected final Object source;
    protected final Date date;
    protected EventType type;//переделать в ENUM

    protected T oldValue;
    protected T newValue;

    NotificationAbstractEvent(Object source, Date date, T oldValue, T newValue, EventType type) {
        this.source = source;
        this.date = date;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = type;
    }

    public NotificationEvent toEntity() throws JsonProcessingException {
        Map<String, T> map = new HashMap<>();
        map.put("old", oldValue);
        map.put("new", newValue);
        return new NotificationEvent(this.date, this.type, new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));
    }
}
