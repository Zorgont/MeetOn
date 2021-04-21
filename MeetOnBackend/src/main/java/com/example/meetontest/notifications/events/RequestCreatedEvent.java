package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.RequestDTO;

import java.util.Date;

public class RequestCreatedEvent extends NotificationAbstractEvent<RequestDTO> {
    public RequestCreatedEvent(Object source, Date date, RequestDTO request) {
        super(source, date, null, request);
    }
}
