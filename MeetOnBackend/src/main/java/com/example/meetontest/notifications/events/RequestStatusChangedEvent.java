package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.entities.User;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.entities.NotificationEventStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;

public class RequestStatusChangedEvent extends NotificationAbstractEvent<RequestDTO> {
    private NotificationEvent event;

    public RequestStatusChangedEvent(Object source, Date date, RequestDTO oldValue, RequestDTO newValue) {
        super(source, date, oldValue, newValue);
    }

    public RequestStatusChangedEvent(NotificationEvent event) throws JsonProcessingException {
        super(null, new Date(),
                new ObjectMapper().readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {}).get("old"),
                new ObjectMapper().readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {}).get("new"));

        this.event = event;
    }

    @Override
    public void process() {
        requestChanged(newValue, RequestStatus.valueOf(oldValue.getStatus()), RequestStatus.valueOf(newValue.getStatus()));
        event.setStatus(NotificationEventStatus.SENT);
        notificationEventStoringService.updateEvent(event);
    }

    private void requestChanged(RequestDTO request, RequestStatus oldStatus, RequestStatus newStatus) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Your request on meeting ")
                .append(meetingService.getMeetingById(request.getMeeting_id()).getName())
                .append(" was changed from ")
                .append(oldStatus.toString().toLowerCase())
                .append(" to ")
                .append(newStatus.toString().toLowerCase());

        User user = userService.getUserById(request.getUser_id());
        mailService.sendSimpleMessage(user.getEmail(), "Request changing", stringBuilder.toString());
        notificationService.createNotification(new Notification(new Date(), stringBuilder.toString(), user));
    }
}
