package com.example.meetontest.notifications.services;

import com.example.meetontest.entities.User;

import com.example.meetontest.exceptions.ResourceNotFoundException;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationStatus;
import com.example.meetontest.notifications.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private boolean isChanged = false;
    @Override
    public Notification getById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found!"));
    }

    @Override
    public List<Notification> getByUser(User user) {
        return notificationRepository.findByUser(user).stream().sorted((note1, note2) -> note1.getDate().before(note2.getDate()) ? 1 : -1).collect(Collectors.toList());
    }

    @Override
    public List<Notification> getByUserAndStatus(User user, NotificationStatus status) {
        return status!=null?notificationRepository.findByUserAndStatus(user,status).stream()
                .sorted((note1, note2) -> note1.getDate().before(note2.getDate()) ? 1 : -1).collect(Collectors.toList()):getByUser(user);
    }


    @Override
    public void changeNotificationsStatus(Notification notification, NotificationStatus status) {
        notification.setStatus(status);
        notificationRepository.save(notification);
    }

    @Override
    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
    }


    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
