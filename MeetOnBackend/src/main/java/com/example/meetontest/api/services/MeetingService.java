package com.example.meetontest.api.services;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.exceptions.ValidatorException;



import java.util.List;

public interface MeetingService {
    public List<Meeting> getMeetingsByTags(List<String> tags);
    public Meeting createMeeting (Meeting meetingRequest) throws ValidatorException;
    public Meeting getMeetingById(Long id);
    public boolean deleteMeeting(Long id);
    public Meeting updateMeeting(Long id,Meeting meetingRequest) throws ValidatorException;
    public List<Meeting> getMeetingsByManager(User manager);
}
