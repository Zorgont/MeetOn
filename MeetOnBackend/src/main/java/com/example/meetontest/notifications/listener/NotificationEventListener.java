package com.example.meetontest.notifications.listener;

import com.example.meetontest.notifications.events.MeetingChangedEvent;
import com.example.meetontest.notifications.services.MeetingNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener{

    private final MeetingNotificationService meetingNotificationService;

    @EventListener
    @Async
    public void handleMeetingChange(MeetingChangedEvent event) {
        try {

                meetingNotificationService.parseData(((MeetingChangedEvent) event).getOldValue(),((MeetingChangedEvent) event).getNewValue());

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
