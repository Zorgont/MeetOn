package com.example.meetontest.sheduling;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.services.MeetingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingStatusChanger {

    private static final Logger log = LoggerFactory.getLogger(MeetingStatusChanger.class);
    private final MeetingService meetingService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void changeStatus() {
        meetingService.getMeetingsByTags(null).forEach(meeting -> {
            if(dateFormat.format(meeting.getDate()).equals(dateFormat.format(new Date()))) {
                meeting.setStatus(MeetingStatus.IN_PROGRESS);
                log.info("Changed status of meeting " + meeting.getName() + " to " + meeting.getStatus());
                meetingService.updateMeeting(meeting.getId(), meeting);
                emailService.sendSimpleMessage("kirill.petuhov.00@mail.ru","Test","Changed status of meeting " + meeting.getName() + " to " + meeting.getStatus());
            }
            if(dateFormat.format(meeting.getEndDate()).equals(dateFormat.format(new Date()))) {
                meeting.setStatus(MeetingStatus.FINISHED);
                log.info("Changed status of meeting " + meeting.getName() + " to " + meeting.getStatus());
                meetingService.updateMeeting(meeting.getId(), meeting);
                emailService.sendSimpleMessage("kirill.petuhov.00@mail.ru","Test","Changed status of meeting " + meeting.getName() + " to " + meeting.getStatus());
            }
        });

    }
}