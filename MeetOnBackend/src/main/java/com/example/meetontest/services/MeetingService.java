package com.example.meetontest.services;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ValidatorException;
import java.util.List;

public interface MeetingService {
    List<Meeting> getMeetingsByTags(List<String> tags);
    Meeting createMeeting (Meeting meetingRequest) throws ValidatorException;
    Meeting getMeetingById(Long id);
    boolean deleteMeeting(Long id);
    Meeting updateMeeting(Long id,Meeting meetingRequest) throws ValidatorException;
    List<Meeting> getMeetingsByManager(User manager);
}