package com.example.meetontest.api.services;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.exceptions.ResourceNotFoundException;
import com.example.meetontest.api.exceptions.ValidatorException;
import com.example.meetontest.api.repositories.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService{
    private final MeetingRepository meetingRepository;
    private final TagService tagService;
    private final MeetingValidator meetingValidator;

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

        Meeting meeting=meetingRepository.findById(id).get();
        meeting.setName(meetingRequest.getName());
        meeting.setAbout(meetingRequest.getAbout());
        meeting.setDate(meetingRequest.getDate());
        meeting.setIsParticipantAmountRestricted(meetingRequest.getIsParticipantAmountRestricted());
        meeting.setParticipantAmount(meetingRequest.getParticipantAmount());
        meeting.setIsPrivate(meetingRequest.getIsPrivate());
        meeting.setDetails(meetingRequest.getDetails());
        meeting.setManager(meetingRequest.getManager());
        meeting.setTags(meetingRequest.getTags());
        return meetingRepository.save(meeting);
    }

    public List<Meeting> getMeetingsByManager(User manager) {
        return meetingRepository.findByManager(manager);
    }
}
