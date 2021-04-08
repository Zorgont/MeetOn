package com.example.meetontest.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Date endDate;
    private String about;
    private Boolean isParticipantAmountRestricted;
    private int participantAmount;
    private Boolean isPrivate;
    private String details;

    @Enumerated(EnumType.STRING)
    private MeetingStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "meeting_tags",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "meeting")
    @JsonIgnore
    private List<Request> requests;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
    @JsonIgnore
    private List<Comment> comments;

    public Meeting(String name, Date date, Date endDate, String about, boolean isParticipantAmountRestricted, int participantAmount, boolean isPrivate, String details, MeetingStatus status, User manager, Set<Tag> tags) {
        this.name = name;
        this.date = date;
        this.endDate = endDate;
        this.about = about;
        this.isParticipantAmountRestricted = isParticipantAmountRestricted;
        this.participantAmount = participantAmount;
        this.isPrivate = isPrivate;
        this.details = details;
        this.status = status;
        this.manager = manager;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", endDate=" + endDate +
                ", about='" + about + '\'' +
                ", isParticipantAmountRestricted=" + isParticipantAmountRestricted +
                ", participantAmount=" + participantAmount +
                ", isPrivate=" + isPrivate +
                ", details='" + details + '\'' +
                ", status=" + status +
                ", manager=" + manager +
                '}';
    }
}