package com.example.meetontest.notifications.services;

import com.example.meetontest.entities.User;

import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationStatus;
import com.example.meetontest.notifications.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getByUser(User user) {
        return notificationRepository.findByUser(user);
    }

    @Override
    public List<Notification> getByUserAndStatus(User user, NotificationStatus status) {
        return status!=null?notificationRepository.findByUserAndStatus(user,status):getByUser(user);
    }
}
