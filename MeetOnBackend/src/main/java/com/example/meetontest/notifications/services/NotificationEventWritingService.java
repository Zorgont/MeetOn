package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.events.NotificationAbstractEvent;
import com.example.meetontest.notifications.repositories.NotificationEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventWritingService {

    private final NotificationEventRepository notificationEventRepository;

    @EventListener
    public void handleEvent(NotificationAbstractEvent event) {
        try {
           notificationEventRepository.save(event.toEntity());
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
