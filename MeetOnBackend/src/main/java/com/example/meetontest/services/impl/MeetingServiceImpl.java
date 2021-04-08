package com.example.meetontest.services.impl;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.Tag;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ResourceNotFoundException;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.notifications.events.MeetingChangedEvent;
import com.example.meetontest.repositories.MeetingRepository;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.MeetingValidator;
import com.example.meetontest.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final TagService tagService;
    private final MeetingValidator meetingValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<Meeting> getMeetingsByTags(List<String> tags) {
        if (tags == null || tags.isEmpty())
            return meetingRepository.findAll();

        Set<Tag> tagsSet = tagService.getTags(tags);
        Tag first=tagsSet.iterator().next();
        List<Meeting> filteredMeetings=meetingRepository.findByTags(first);
        tagsSet.remove(first);
        for(Tag item:tagsSet){
            filteredMeetings.retainAll(meetingRepository.findByTags(item));
        }

        return filteredMeetings;
    }


    public Meeting createMeeting(Meeting meetingRequest) throws ValidatorException {
        meetingValidator.validate(meetingRequest);
        return meetingRepository.save(meetingRequest);
    }

    public Meeting getMeetingById(Long id) {
        return meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meeting no exist!"));
    }
    public boolean deleteMeeting(Long id){
        meetingRepository.deleteById(id);
        return true;
    }

    public Meeting updateMeeting(Long id,Meeting meetingRequest) throws ValidatorException {
            meetingValidator.validate(meetingRequest);
            Meeting meeting = meetingRepository.findById(id).get();
            Meeting oldMeeting=new Meeting(meeting.getName(),meeting.getDate(),meeting.getEndDate(),meeting.getAbout(),
                    meeting.getIsParticipantAmountRestricted(),meeting.getParticipantAmount(),meeting.getIsPrivate(),
                    meeting.getDetails(),meeting.getStatus(),meeting.getManager(),meeting.getTags());
            meeting.setName(meetingRequest.getName());
            meeting.setAbout(meetingRequest.getAbout());
            meeting.setDate(meetingRequest.getDate());
            meeting.setIsParticipantAmountRestricted(meetingRequest.getIsParticipantAmountRestricted());
            meeting.setParticipantAmount(meetingRequest.getParticipantAmount());
            meeting.setIsPrivate(meetingRequest.getIsPrivate());
            meeting.setStatus(meetingRequest.getStatus());
            meeting.setDetails(meetingRequest.getDetails());
            meeting.setManager(meetingRequest.getManager());
            meeting.setTags(meetingRequest.getTags());
//            meeting.setRequests(meetingRequest.getRequests());

            meetingRepository.save(meeting);

            MeetingChangedEvent event = new MeetingChangedEvent(this,oldMeeting,meeting,new Date());
            applicationEventPublisher.publishEvent(event);

            return meeting;

    }

    public List<Meeting> getMeetingsByManager(User manager) {
        return meetingRepository.findByManager(manager);
    }
}