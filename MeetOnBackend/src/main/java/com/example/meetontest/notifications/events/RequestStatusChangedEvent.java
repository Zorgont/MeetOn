package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.RequestDTO;

import java.util.Date;

public class RequestStatusChangedEvent extends NotificationAbstractEvent<RequestDTO> {
    public RequestStatusChangedEvent(Object source, Date date, RequestDTO oldValue, RequestDTO newValue) {
        super(source, date, oldValue, newValue);
    }
}
