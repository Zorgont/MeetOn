package com.example.meetontest.notifications.services;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.*;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationEventStatus;
import com.example.meetontest.notifications.events.MeetingChangedEvent;
import com.example.meetontest.notifications.events.RequestCreatedEvent;
import com.example.meetontest.notifications.events.RequestStatusChangedEvent;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationSendingService {
    private static final Logger log = LoggerFactory.getLogger(NotificationSendingService.class);

    private final NotificationEventStoringService notificationEventStoringService;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final MeetingConverter meetingConverter;
    private final MeetingService meetingService;
    private final RequestService requestService;
    private final UserService userService;

    @Scheduled(fixedRate = 10000)
    public void checkEvents() {
        notificationEventStoringService.getUnsentEventsList().forEach(notificationEvent -> {
            try {
                if (notificationEvent.getType().equals(MeetingChangedEvent.class.getSimpleName())) {
                    Map<String, MeetingDTO> map = new ObjectMapper().readValue(notificationEvent.getBody(), new TypeReference<Map<String, MeetingDTO>>() {
                    });
                    MeetingDTO newValueDTO = map.get("new");
                    Meeting oldValue = meetingConverter.convert(map.get("old"));
                    Meeting newValue = meetingConverter.convert(newValueDTO);
                    newValue.setId(newValueDTO.getMeetingId());
                    if (oldValue.getStatus() != newValue.getStatus())
                        statusChanged(newValue);
                    else infoChanged(oldValue, newValue);

                    notificationEvent.setStatus(NotificationEventStatus.SENT);
                    notificationEventStoringService.updateEvent(notificationEvent);
                } else if (notificationEvent.getType().equals(RequestStatusChangedEvent.class.getSimpleName())) {
                    Map<String, RequestDTO> map = new ObjectMapper().readValue(notificationEvent.getBody(), new TypeReference<Map<String, RequestDTO>>() {
                    });

                    RequestDTO request = map.get("new");
                    RequestStatus oldStatus = RequestStatus.valueOf(map.get("old").getStatus());
                    RequestStatus newStatus = RequestStatus.valueOf(request.getStatus());
                    requestChanged(request, oldStatus, newStatus);

                    notificationEvent.setStatus(NotificationEventStatus.SENT);
                    notificationEventStoringService.updateEvent(notificationEvent);
                }
            } catch (JsonProcessingException | ParseException e) {
                e.printStackTrace();
            }
        });
    }

    // once per 1 min.
    @Scheduled(fixedRate = 60000)
    public void checkUnsentRequestCreatedEvents() {
        notificationEventStoringService.getUnsentEventsList().stream().filter(event -> event.getType().equals(RequestCreatedEvent.class.getSimpleName()))
                .collect(Collectors.groupingBy(event -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, RequestDTO> map = objectMapper.readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {
                        });

                        return meetingService.getMeetingById(map.get("new").getMeeting_id()).getId();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }))
                .forEach((meetingId, events) -> {
                    Meeting meeting = meetingService.getMeetingById(meetingId);
                    emailService.sendSimpleMessage(meetingService.getManager(meeting).getEmail(), "New requests for meeting " + meeting.getName(), "You have new " + events.size() + " requests on meeting " + meeting.getName());
                    events.forEach(event -> {
                        event.setStatus(NotificationEventStatus.SENT);
                        notificationEventStoringService.updateEvent(event);
                    });
                    notificationService.createNotification(new Notification(new Date(), events.size() + " new requests on meeting " + meeting.getName(), meetingService.getManager(meeting)));
                });
    }

    public void statusChanged(Meeting meeting) {
        if (meeting.getStatus() == MeetingStatus.CANCELED) {
            requestService.getByMeeting(meeting).forEach(request -> {
                emailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + meeting.getName() + " cancelled", meeting.toString());
                notificationService.createNotification(new Notification(new Date(), "Meeting " + meeting.getName() + " cancelled", request.getUser()));
            });

        } else if (meeting.getStatus() == MeetingStatus.IN_PROGRESS) {
            requestService.getByMeeting(meeting).forEach(request -> {
                emailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + meeting.getName() + " began", meeting.toString());
                notificationService.createNotification(new Notification(new Date(), "Meeting " + meeting.getName() + " began", request.getUser()));
            });

        } else if (meeting.getStatus() == MeetingStatus.FINISHED) {
            requestService.getByMeeting(meeting).forEach(request -> {
                emailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + meeting.getName() + " finished", meeting.toString());
                notificationService.createNotification(new Notification(new Date(), "Meeting " + meeting.getName() + " finished", request.getUser()));
            });

        }
    }

    public void infoChanged(Meeting oldMeeting, Meeting newMeeting) {
        StringBuilder stringBuilder = new StringBuilder("Information of this meeting was changed:" + '\n');
        if (!oldMeeting.getName().equals(newMeeting.getName()))
            stringBuilder.append("Name from ").append(oldMeeting.getName()).append(" to ").append(newMeeting.getName()).append('\n');
        if (!oldMeeting.getDate().equals(newMeeting.getDate()))
            stringBuilder.append("Date from ").append(oldMeeting.getDate()).append(" to ").append(newMeeting.getDate()).append('\n');
        if (!oldMeeting.getEndDate().equals(newMeeting.getEndDate()))
            stringBuilder.append("End date from ").append(oldMeeting.getEndDate()).append(" to ").append(newMeeting.getEndDate()).append('\n');
        if (!(oldMeeting.getParticipantAmount() == (newMeeting.getParticipantAmount())))
            stringBuilder.append("Participant Amount from ").append(oldMeeting.getParticipantAmount()).append(" to ").append(newMeeting.getParticipantAmount()).append('\n');
        if (!(oldMeeting.getIsPrivate() == (newMeeting.getIsPrivate())))
            stringBuilder.append("Private from ").append(oldMeeting.getIsPrivate()).append(" to ").append(newMeeting.getIsPrivate()).append('\n');
        if (!(oldMeeting.getIsParticipantAmountRestricted() == (newMeeting.getIsParticipantAmountRestricted())))
            stringBuilder.append("Participant Amount Restricted from ").append(oldMeeting.getIsParticipantAmountRestricted()).append(" to ").append(newMeeting.getIsParticipantAmountRestricted()).append('\n');
        if (!oldMeeting.getAbout().equals(newMeeting.getAbout()))
            stringBuilder.append("About from ").append(oldMeeting.getAbout()).append(" to ").append(newMeeting.getAbout()).append('\n');
        if (!oldMeeting.getDetails().equals(newMeeting.getDetails()))
            stringBuilder.append("Details from ").append(oldMeeting.getDetails()).append(" to ").append(newMeeting.getDetails()).append('\n');

        //tags
        log.info(stringBuilder.toString());
        List<Request> requestList = requestService.getByMeeting(meetingService.getMeetingById(newMeeting.getId()));
        requestList.forEach(request -> {
            emailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + newMeeting.getName() + " changed", stringBuilder.toString());
            notificationService.createNotification(new Notification(new Date(), "Meeting " + newMeeting.getName() + " changed", request.getUser()));
        });
    }

    public void requestChanged(RequestDTO request, RequestStatus oldStatus, RequestStatus newStatus) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Your request on meeting ")
                .append(meetingService.getMeetingById(request.getMeeting_id()).getName())
                .append(" was changed from ")
                .append(oldStatus.toString().toLowerCase())
                .append(" to ")
                .append(newStatus.toString().toLowerCase());

        User user = userService.getUserById(request.getUser_id());
        emailService.sendSimpleMessage(user.getEmail(), "Request changing", stringBuilder.toString());
        notificationService.createNotification(new Notification(new Date(), stringBuilder.toString(), user));
    }
}
