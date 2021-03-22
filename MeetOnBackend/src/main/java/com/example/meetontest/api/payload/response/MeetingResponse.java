package com.example.meetontest.api.payload.response;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor
public class MeetingResponse extends CreateMeetingRequest {
    private Long meetingId;
    private Long managerId;

    public MeetingResponse(Long id, String name, Date date, String about, String details, Long managerId, String managerUsername, boolean isPrivate, boolean isParticipantAmountRestricted, int participantAmount, Set<String> tags) {
        this.meetingId = id;
        this.managerId = managerId;
        super.setName(name);
        super.setDate(date);
        super.setAbout(about);
        super.setDetails(details);
        super.setManagerUsername(managerUsername);
        super.setIsPrivate(isPrivate ? 1 : 0);
        super.setIsParticipantAmountRestricted(isParticipantAmountRestricted ? 1 : 0);
        super.setParticipantAmount(participantAmount);
        super.setTags(tags);
    }

    public MeetingResponse(Long id, Long managerId, CreateMeetingRequest meetingRequest) {
        this(id,
             meetingRequest.getName(),
             meetingRequest.getDate(),
             meetingRequest.getAbout(),
             meetingRequest.getDetails(),
             managerId,
             meetingRequest.getManagerUsername(),
             meetingRequest.getIsPrivate() == 1,
             meetingRequest.getIsParticipantAmountRestricted() == 1,
             meetingRequest.getParticipantAmount(),
             meetingRequest.getTags());
    }

    public MeetingResponse(Meeting meeting) {
        this(meeting.getId(),
             meeting.getName(),
             meeting.getDate(),
             meeting.getAbout(),
             meeting.getDetails(),
             meeting.getManager().getId(),
             meeting.getManager().getUsername(),
             meeting.isPrivate(),
             meeting.isParticipantAmountRestricted(),
             meeting.getParticipantAmount(),
             meeting.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toSet()));
    }
}
