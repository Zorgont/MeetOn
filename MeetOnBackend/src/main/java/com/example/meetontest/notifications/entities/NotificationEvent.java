package com.example.meetontest.notifications.entities;

import com.example.meetontest.notifications.events.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notification_events")
@Getter
@Setter
@NoArgsConstructor
public class NotificationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    @Enumerated(EnumType.STRING)
    private EventType type;//переделать в ENUM
    @Column(columnDefinition="text")
    private String body;
    @Enumerated(EnumType.STRING)
    private NotificationEventStatus status;//ENUM
    public NotificationEvent(Date date, EventType type, String body) {
        this.date = date;
        this.type = type;
        this.body = body;
        this.status=NotificationEventStatus.UNSENT;
    }
}
