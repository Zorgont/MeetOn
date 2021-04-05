package com.example.meetontest.notifications.listener;

import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.events.MeetingChangedEvent;
import com.example.meetontest.notifications.services.MeetingNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener implements ApplicationListener<MeetingChangedEvent> {

    private final MeetingNotificationService meetingNotificationService;
    @Override
    public void onApplicationEvent(MeetingChangedEvent event) {
        try {
//            if(event instanceof MeetingChangedEvent) {
                meetingNotificationService.parseData(((MeetingChangedEvent) event).getOldValue(),((MeetingChangedEvent) event).getNewValue());
//            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
