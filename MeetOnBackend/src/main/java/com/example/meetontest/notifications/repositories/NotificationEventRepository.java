package com.example.meetontest.notifications.repositories;

import com.example.meetontest.notifications.entities.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationEventRepository extends JpaRepository<NotificationEvent,Long> {
    List <NotificationEvent> findByStatus(String status);
}
