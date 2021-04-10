package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.entities.NotificationEvent;
import com.example.meetontest.notifications.entities.NotificationEventStatus;
import com.example.meetontest.notifications.events.NotificationAbstractEvent;
import com.example.meetontest.notifications.repositories.NotificationEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventStoringServiceImpl implements NotificationEventStoringService{

    private final NotificationEventRepository notificationEventRepository;

    public void saveEvent(NotificationAbstractEvent event) {
        try {
           notificationEventRepository.save(event.toEntity());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<NotificationEvent> getUnsentEventsList(){
        return notificationEventRepository.findByStatus(NotificationEventStatus.UNSENT);
    }

    @Override
    public void updateEvent(NotificationEvent event) {
        try {
            notificationEventRepository.save(event);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
