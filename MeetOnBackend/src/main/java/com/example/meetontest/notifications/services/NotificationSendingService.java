package com.example.meetontest.notifications.services;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.entities.Request;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.repositories.NotificationEventRepository;
import com.example.meetontest.services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationSendingService {
    private final EmailService emailService;
    private final RequestService requestService;
    private final NotificationEventRepository notificationEventRepository;
    private static final Logger log = LoggerFactory.getLogger(NotificationSendingService.class);

    @Scheduled(fixedRate = 60000)
    public void checkEvents(){
        notificationEventRepository.findByStatus("UNSENT").forEach(notificationEvent -> {
            try {
            ObjectMapper objectMapper = new ObjectMapper();
            if(notificationEvent.getType().equals("MEETING_CHANGED")){
                    Map<String,Meeting> map=objectMapper.readValue(notificationEvent.getBody(),new TypeReference<Map<String, Meeting>>() {});

                    Meeting oldValue = map.get("old");
                    Meeting newValue = map.get("new");
                    if(oldValue.getStatus()!=newValue.getStatus())
                        statusChanged(newValue);
                    else infoChanged(oldValue,newValue);
            }
            notificationEvent.setStatus("SENT");
            notificationEventRepository.save(notificationEvent);
            } catch (JsonProcessingException e) {
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
        List<Request> requestList= requestService.getByMeeting(newMeeting);
        requestList.forEach(request ->
        {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + newMeeting.getName() + " changed", stringBuilder.toString());
        });
    }
}
