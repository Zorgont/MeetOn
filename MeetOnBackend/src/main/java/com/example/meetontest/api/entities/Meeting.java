package com.example.meetontest.api.entities;

import javax.persistence.*;
import java.util.Date;
import lombok.*;

@Entity
@Table(name = "meetings")
@Setter @Getter @NoArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;

    private String name;
    private Date date;
    private String about;
    private int participantAmount;
    private boolean isPrivate;
    private String details;
    private String status;


    public Meeting(User manager, String name, Date date, String about, int participantAmount, boolean isPrivate, String details, String status) {
        this.manager = manager;
        this.name = name;
        this.date = date;
        this.about = about;
        this.participantAmount = participantAmount;
        this.isPrivate = isPrivate;
        this.details = details;
        this.status = status;
    }
}
