package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestStatusChangedEvent extends NotificationAbstractEvent {
    private final RequestDTO oldValue;
    private final RequestDTO newValue;

    public RequestStatusChangedEvent(Date date, RequestDTO oldValue, RequestDTO newValue) {
        super(date);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = EventType.REQUEST_STATUS_CHANGED;
    }

    @Override
    public NotificationEvent toEntity() throws JsonProcessingException {
        Map<String, RequestDTO> map = new HashMap<>();
        map.put("old", oldValue);
        map.put("new", newValue);
        return new NotificationEvent(this.date, this.type, new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));
    }
}
