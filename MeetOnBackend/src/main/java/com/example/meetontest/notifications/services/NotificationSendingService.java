package com.example.meetontest.notifications.services;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.entities.Request;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.NotificationEventStatus;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationSendingService {
    private final EmailService emailService;
    private final RequestService requestService;
    private final NotificationEventStoringService notificationEventStoringService;
    private final MeetingConverter meetingConverter;
    private final MeetingService meetingService;
    private static final Logger log = LoggerFactory.getLogger(NotificationSendingService.class);

    @Scheduled(fixedRate = 10000)
    public void checkEvents(){
        notificationEventStoringService.getUnsentEventsList().forEach(notificationEvent -> {
            try {
            ObjectMapper objectMapper = new ObjectMapper();
            if(notificationEvent.getType().equals("MEETING_CHANGED")){
                    Map<String, MeetingDTO> map=objectMapper.readValue(notificationEvent.getBody(),new TypeReference<Map<String, MeetingDTO>>() {});
                    MeetingDTO newValueDTO = map.get("new");
                    Meeting oldValue=meetingConverter.convert(map.get("old"));
                    Meeting newValue=meetingConverter.convert(newValueDTO);
                    newValue.setId(newValueDTO.getMeetingId());
                    if(oldValue.getStatus()!=newValue.getStatus())
                        statusChanged(newValue);
                    else infoChanged(oldValue,newValue);
            }
            notificationEvent.setStatus(NotificationEventStatus.SENT);
            notificationEventStoringService.updateEvent(notificationEvent);
            } catch (JsonProcessingException | ParseException e) {
                e.printStackTrace();
            }
        });

    }

    public void statusChanged(Meeting meeting){
        if(meeting.getStatus() == MeetingStatus.CANCELED){
            requestService.getByMeeting(meeting).forEach(request ->
            {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + meeting.getName() + " cancelled",meeting.toString());});
            emailService.sendSimpleMessage(meeting.getManager().getEmail(),"Meeting " + meeting.getName() + " cancelled",meeting.toString());
        }
        if(meeting.getStatus() == MeetingStatus.IN_PROGRESS){
            requestService.getByMeeting(meeting).forEach(request ->
            {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + meeting.getName() + " began",meeting.toString());});
            emailService.sendSimpleMessage(meeting.getManager().getEmail(),"Meeting " + meeting.getName() + " began",meeting.toString());
        }
        if(meeting.getStatus() == MeetingStatus.FINISHED){
            requestService.getByMeeting(meeting).forEach(request ->
            {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + meeting.getName() + " finished",meeting.toString());});
            emailService.sendSimpleMessage(meeting.getManager().getEmail(),"Meeting " + meeting.getName() + " finished",meeting.toString());
        }

    }
    public void infoChanged(Meeting oldMeeting,Meeting newMeeting){
        StringBuilder stringBuilder=new StringBuilder("Information of this meeting was changed:"+'\n');
        if(!oldMeeting.getName().equals(newMeeting.getName()))
            stringBuilder.append("Name from ").append(oldMeeting.getName()).append(" to ").append(newMeeting.getName()).append('\n');
        if(!oldMeeting.getDate().equals(newMeeting.getDate()))
            stringBuilder.append("Date from ").append(oldMeeting.getDate()).append(" to ").append(newMeeting.getDate()).append('\n');
        if(!oldMeeting.getEndDate().equals(newMeeting.getEndDate()))
            stringBuilder.append("End date from ").append(oldMeeting.getEndDate()).append(" to ").append(newMeeting.getEndDate()).append('\n');
        if(!(oldMeeting.getParticipantAmount()==(newMeeting.getParticipantAmount())))
            stringBuilder.append("Participant Amount from ").append(oldMeeting.getParticipantAmount()).append(" to ").append(newMeeting.getParticipantAmount()).append('\n');
        if(!(oldMeeting.getIsPrivate()==(newMeeting.getIsPrivate())))
            stringBuilder.append("Private from ").append(oldMeeting.getIsPrivate()).append(" to ").append(newMeeting.getIsPrivate()).append('\n');
        if(!(oldMeeting.getIsParticipantAmountRestricted()==(newMeeting.getIsParticipantAmountRestricted())))
            stringBuilder.append("Participant Amount Restricted from ").append(oldMeeting.getIsParticipantAmountRestricted()).append(" to ").append(newMeeting.getIsParticipantAmountRestricted()).append('\n');
        if(!oldMeeting.getAbout().equals(newMeeting.getAbout()))
            stringBuilder.append("About from ").append(oldMeeting.getAbout()).append(" to ").append(newMeeting.getAbout()).append('\n');
        if(!oldMeeting.getDetails().equals(newMeeting.getDetails()))
            stringBuilder.append("Details from ").append(oldMeeting.getDetails()).append(" to ").append(newMeeting.getDetails()).append('\n');
        //tags
        log.info(stringBuilder.toString());
        List<Request> requestList= requestService.getByMeeting(meetingService.getMeetingById(newMeeting.getId()));
        requestList.forEach(request ->
        {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + newMeeting.getName() + " changed", stringBuilder.toString());
        });
    }
}
