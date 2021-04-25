package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.Request;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class RequestCreatedEvent implements AbstractNotificationEvent<RequestDTO> {

    @Override
    public void process(NotificationEvent event) throws JsonProcessingException, ParseException {

    }
}
