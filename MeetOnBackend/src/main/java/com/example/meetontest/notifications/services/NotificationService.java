package com.example.meetontest.notifications.services;

import com.example.meetontest.entities.User;
import com.example.meetontest.notifications.dto.NotificationDTO;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationStatus;

import java.util.List;

public interface NotificationService {
    List<Notification> getByUser(User user);
    List<Notification> getByUserAndStatus(User user, NotificationStatus status);
}