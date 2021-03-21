package com.example.meetontest.api.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Entity
@Table(name = "meetings")
@Getter @Setter @NoArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date date;
    private String about;
    private boolean isParticipantAmountRestricted;
    private int participantAmount;
    private boolean isPrivate;
    private String details;
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "meeting_tags",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public Meeting(String name, Date date, String about, boolean isParticipantAmountRestricted, int participantAmount, boolean isPrivate, String details, String status, User manager, Set<Tag> tags) {
        this.name = name;
        this.date = date;
        this.about = about;
        this.isParticipantAmountRestricted = isParticipantAmountRestricted;
        this.participantAmount = participantAmount;
        this.isPrivate = isPrivate;
        this.details = details;
        this.status = status;
        this.manager = manager;
        this.tags = tags;
    }
}
