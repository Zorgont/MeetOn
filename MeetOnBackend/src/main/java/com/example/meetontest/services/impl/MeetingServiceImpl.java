package com.example.meetontest.services.impl;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.entities.*;
import com.example.meetontest.exceptions.ResourceNotFoundException;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.notifications.events.MeetingChangedEvent;
import com.example.meetontest.notifications.services.NotificationEventStoringService;
import com.example.meetontest.repositories.MeetingRepository;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.MeetingValidator;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final TagService tagService;
    private final MeetingValidator meetingValidator;
    private final NotificationEventStoringService notificationEventStoringService;

    @Autowired
    private MeetingConverter meetingConverter;

    @Autowired
    private RequestService requestService;

    public List<Meeting> getMeetingsByTags(List<String> tags) {
        if (tags == null || tags.isEmpty())
            return meetingRepository.findAll();

        Set<Tag> tagsSet = tagService.getTags(tags);
        Tag first = tagsSet.iterator().next();
        List<Meeting> filteredMeetings = meetingRepository.findByTags(first);
        tagsSet.remove(first);

        for(Tag item:tagsSet)
            filteredMeetings.retainAll(meetingRepository.findByTags(item));

        return filteredMeetings;
    }

    @Transactional
    public Meeting createMeeting(Meeting meetingRequest, User manager) throws ValidatorException {
        meetingValidator.validate(meetingRequest);
        Meeting meeting = meetingRepository.save(meetingRequest);
        requestService.create(new Request(meeting, manager, MeetingRole.MANAGER, RequestStatus.APPROVED));
        return meeting;
    }

    public User getManager(Meeting meeting) {
        return requestService.getByMeeting(meeting).stream().filter(request -> request.getRole() == MeetingRole.MANAGER).findFirst().get().getUser();
    }

    public Meeting getMeetingById(Long id) {
        return meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meeting no exist!"));
    }

    public boolean deleteMeeting(Long id){
        meetingRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Meeting updateMeeting(Long id, Meeting meetingRequest) throws ValidatorException {
        meetingValidator.validate(meetingRequest);
        Meeting meeting = meetingRepository.findById(id).get();

        //Hibernate возвращает на один и тот же объект,поэтому клонируем старый митинг
        Meeting oldMeeting = new Meeting(meeting.getName(), meeting.getDate(), meeting.getEndDate(), meeting.getAbout(),
                meeting.getIsParticipantAmountRestricted(), meeting.getParticipantAmount(), meeting.getIsPrivate(),
                meeting.getDetails(), meeting.getStatus(), meeting.getTags());
        oldMeeting.setId(meeting.getId());

        meeting.setName(meetingRequest.getName());
        meeting.setAbout(meetingRequest.getAbout());
        meeting.setDate(meetingRequest.getDate());
        meeting.setIsParticipantAmountRestricted(meetingRequest.getIsParticipantAmountRestricted());
        meeting.setParticipantAmount(meetingRequest.getParticipantAmount());
        meeting.setIsPrivate(meetingRequest.getIsPrivate());
        meeting.setStatus(meetingRequest.getStatus());
        meeting.setDetails(meetingRequest.getDetails());
        meeting.setTags(meetingRequest.getTags());

        meetingRepository.save(meeting);
        notificationEventStoringService.saveEvent(new MeetingChangedEvent(this, new Date(), meetingConverter.convertBack(oldMeeting), meetingConverter.convertBack(meeting)));
        return meeting;
    }

    public List<Meeting> getMeetingsByManager(User manager) {
        return requestService.getByUser(manager).stream().filter(request -> request.getRole() == MeetingRole.MANAGER).map(Request::getMeeting).collect(Collectors.toList());
    }
}