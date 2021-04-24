package com.example.meetontest.notifications.events;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.services.NotificationEventStoringService;
import com.example.meetontest.notifications.services.NotificationService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class NotificationAbstractEvent<T> {
    protected final Object source;
    protected final Date date;

    protected final T oldValue;
    protected final T newValue;

    protected UserService userService;
    protected MeetingService meetingService;
    protected MeetingConverter meetingConverter;
    protected RequestService requestService;
    protected EmailService mailService;
    protected NotificationService notificationService;
    protected NotificationEventStoringService notificationEventStoringService;

    NotificationAbstractEvent(Object source, Date date, T oldValue, T newValue) {
        this.source = source;
        this.date = date;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public void injectServices(UserService userService, MeetingService meetingService, RequestService requestService, MeetingConverter meetingConverter,
                               EmailService mailService, NotificationService notificationService, NotificationEventStoringService notificationEventStoringService) {
        this.userService = userService;
        this.meetingService = meetingService;
        this.meetingConverter = meetingConverter;
        this.requestService = requestService;
        this.mailService = mailService;
        this.notificationService = notificationService;
        this.notificationEventStoringService = notificationEventStoringService;
    }

    public abstract void process() throws ParseException;

    public NotificationEvent toEntity() throws JsonProcessingException {
        Map<String, T> map = new HashMap<>();
        map.put("old", oldValue);
        map.put("new", newValue);
        System.out.println(this.getClass().getSimpleName());
        return new NotificationEvent(this.date, this.getClass().getSimpleName(), new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));
    }
}
