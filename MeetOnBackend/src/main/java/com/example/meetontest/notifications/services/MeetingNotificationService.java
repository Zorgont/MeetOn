package com.example.meetontest.notifications.services;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingNotificationService {
    private final EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(MeetingNotificationService.class);
    public void parseData(Meeting oldMeeting,Meeting newMeeting){
        if(oldMeeting.getStatus()!= newMeeting.getStatus())
            statusChanged(newMeeting);
        else
            infoChanged(oldMeeting, newMeeting);


    }

    public void statusChanged(Meeting meeting){
        if(meeting.getStatus() == MeetingStatus.CANCELED){
            meeting.getRequests().forEach(request ->
            {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + meeting.getName() + " cancelled",meeting.toString());});
            emailService.sendSimpleMessage(meeting.getManager().getEmail(),"Meeting " + meeting.getName() + " cancelled",meeting.toString());
        }
        if(meeting.getStatus() == MeetingStatus.IN_PROGRESS){
            meeting.getRequests().forEach(request ->
            {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + meeting.getName() + " began",meeting.toString());});
            emailService.sendSimpleMessage(meeting.getManager().getEmail(),"Meeting " + meeting.getName() + " began",meeting.toString());
        }
        if(meeting.getStatus() == MeetingStatus.FINISHED){
            meeting.getRequests().forEach(request ->
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
        if(!oldMeeting.getAbout().equals(newMeeting.getAbout()))
            stringBuilder.append("About from ").append(oldMeeting.getAbout()).append(" to ").append(newMeeting.getAbout()).append('\n');
        if(!oldMeeting.getDetails().equals(newMeeting.getDetails()))
            stringBuilder.append("Details from ").append(oldMeeting.getDetails()).append(" to ").append(newMeeting.getDetails()).append('\n');

        log.info(stringBuilder.toString());
        newMeeting.getRequests().forEach(request ->
        {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + newMeeting.getName() + " changed", stringBuilder.toString());
        });
    }
}
