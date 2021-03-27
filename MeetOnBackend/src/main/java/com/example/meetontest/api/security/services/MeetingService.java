package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import com.example.meetontest.api.payload.response.MeetingResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MeetingService {
    public List<MeetingResponse> getMeetingsByTags(List<String> tags);
    public ResponseEntity<?> createMeeting(CreateMeetingRequest meetingRequest);
    public ResponseEntity<MeetingResponse> getMeetingById(Long id);
    public ResponseEntity<?> deleteMeeting(Long id);
    public ResponseEntity<?> updateMeeting(Long id,CreateMeetingRequest meetingRequest);
    public List<MeetingResponse> getMeetingsByManager(User manager);
}
