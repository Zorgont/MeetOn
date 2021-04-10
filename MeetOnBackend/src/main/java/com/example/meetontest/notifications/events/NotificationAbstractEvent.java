package com.example.meetontest.notifications.events;

import com.example.meetontest.notifications.entities.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;
@Getter
public abstract class NotificationAbstractEvent {

    final Date date;
    String type;//переделать в ENUM

    NotificationAbstractEvent(Date date) {
        this.date = date;
    }
    public NotificationEvent toEntity() throws JsonProcessingException {
        return new NotificationEvent(this.date,this.type,null);
    }

}
