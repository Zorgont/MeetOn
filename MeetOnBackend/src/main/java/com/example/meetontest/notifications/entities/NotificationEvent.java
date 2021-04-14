package com.example.meetontest.notifications.entities;

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
    private String type;

    @Column(columnDefinition="text")
    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationEventStatus status;

    public NotificationEvent(Date date, String type, String body) {
        this.date = date;
        this.type = type;
        this.body = body;
        this.status = NotificationEventStatus.UNSENT;
    }
}
