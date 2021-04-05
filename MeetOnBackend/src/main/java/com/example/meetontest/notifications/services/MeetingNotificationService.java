package com.example.meetontest.notifications.services;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.sheduling.MeetingStatusChanger;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingNotificationService {
    private final EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(MeetingNotificationService.class);
    private final RequestService requestService;
    public void parseData(Meeting oldMeeting,Meeting newMeeting){
        if(oldMeeting.getStatus()!= newMeeting.getStatus())
            statusChanged(newMeeting);
        else
            infoChanged(oldMeeting,newMeeting);
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
        if(!oldMeeting.getAbout().equals(newMeeting.getAbout()))
            stringBuilder.append("About from ").append(oldMeeting.getAbout()).append(" to ").append(newMeeting.getAbout()).append('\n');
        if(!oldMeeting.getDetails().equals(newMeeting.getDetails()))
            stringBuilder.append("Details from ").append(oldMeeting.getDetails()).append(" to ").append(newMeeting.getDetails()).append('\n');

        log.info(stringBuilder.toString());
      requestService.getByMeeting(newMeeting).stream().filter(request -> request.getRequest_id()== newMeeting.getId()).forEach(request ->
        {emailService.sendSimpleMessage(request.getUser().getEmail(),"Meeting " + newMeeting.getName() + " changed", stringBuilder.toString());
        });
    }
}
