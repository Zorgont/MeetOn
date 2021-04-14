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

    protected final T oldValue;
    protected final T newValue;

    NotificationAbstractEvent(Object source, Date date, T oldValue, T newValue) {
        this.source = source;
        this.date = date;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public NotificationEvent toEntity() throws JsonProcessingException {
        Map<String, T> map = new HashMap<>();
        map.put("old", oldValue);
        map.put("new", newValue);
        System.out.println(this.getClass().getSimpleName());
        return new NotificationEvent(this.date, this.getClass().getSimpleName(), new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));
    }
}
