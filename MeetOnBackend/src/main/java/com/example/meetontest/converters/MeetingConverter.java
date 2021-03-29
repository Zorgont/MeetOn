package com.example.meetontest.converters;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.Tag;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.services.TagService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MeetingConverter implements Converter<Meeting, MeetingDTO> {
    private final UserService userService;
    private final TagService tagService;

    @Override
    public Meeting convert(MeetingDTO entity) {
        Meeting meeting=new Meeting();
        meeting.setManager(userService.getUserByName(entity.getManagerUsername()));
        meeting.setName(entity.getName());
        meeting.setAbout(entity.getAbout());
        meeting.setDate(entity.getDate());
        meeting.setIsParticipantAmountRestricted(entity.getIsParticipantAmountRestricted());
        meeting.setParticipantAmount(entity.getParticipantAmount());
        meeting.setIsPrivate(entity.getIsPrivate());
        meeting.setDetails(entity.getDetails());
        meeting.setTags(tagService.getTags(entity.getTags()));
        return meeting;
    }

    @Override
    public MeetingDTO convertBack(Meeting entity) {
        return new MeetingDTO(entity.getId(),
                entity.getName(),
                entity.getDate(),
                entity.getAbout(),
                entity.getDetails(),
                entity.getManager().getId(),
                entity.getManager().getUsername(),
                entity.getIsPrivate(),
                entity.getIsParticipantAmountRestricted(),
                entity.getParticipantAmount(),
                entity.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
    }
}
