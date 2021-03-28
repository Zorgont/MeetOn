package com.example.meetontest.api.payload.response;

import com.example.meetontest.api.entities.Meeting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor
public class MeetingDTO {
    private Long meetingId;
    private Long managerId;
    private String name;
    private Date date;
    private String about;
    private String details;
    private String managerUsername;
    private Boolean isPrivate;
    private Boolean isParticipantAmountRestricted;
    private int participantAmount;
    private List<String> tags;

    public MeetingDTO(Long id, String name, Date date, String about, String details, Long managerId, String managerUsername, boolean isPrivate, boolean isParticipantAmountRestricted, int participantAmount, List<String> tags) {
        this.meetingId = id;
        this.managerId = managerId;
        this.setName(name);
        this.setDate(date);
        this.setAbout(about);
        this.setDetails(details);
        this.setManagerUsername(managerUsername);
        this.setIsPrivate(isPrivate);
        this.setIsParticipantAmountRestricted(isParticipantAmountRestricted);
        this.setParticipantAmount(participantAmount);
        this.setTags(tags);
    }

}
