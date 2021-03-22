package com.example.meetontest.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateMeetingRequest {
    private String name;
    private Date date;
    private String about;
    private String details;
    private String managerUsername;
    private int isPrivate;
    private int isParticipantAmountRestricted;
    private int participantAmount;
    private Set<String> tags;
}
