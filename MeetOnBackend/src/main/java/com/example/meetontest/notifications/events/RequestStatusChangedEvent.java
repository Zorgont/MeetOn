package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestStatusChangedEvent extends NotificationAbstractEvent {

    private final RequestDTO requestDTO;
    private final RequestStatus oldValue;
    private final RequestStatus newValue;

    public RequestStatusChangedEvent(Date date, RequestDTO request, RequestStatus oldValue, RequestStatus newValue) {
        super(date);
        this.requestDTO = request;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = EventType.REQUEST_STATUS_CHANGED;
    }

    @Override
    public NotificationEvent toEntity() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Map<String, String> map = new HashMap<>();
        map.put("requestDTO", objectWriter.writeValueAsString(requestDTO));
        map.put("oldStatusValue",objectWriter.writeValueAsString(oldValue));
        map.put("newStatusValue", objectWriter.writeValueAsString(newValue));
        return new NotificationEvent(this.date, this.type, new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));

    }
}
