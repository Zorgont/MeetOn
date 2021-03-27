package com.example.meetontest.api.security.services;

import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import com.example.meetontest.api.payload.response.MeetingResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MeetingService {
    public List<MeetingResponse> getMeetingsByTags(List<String> tags);
    public ResponseEntity<?> createMeeting(CreateMeetingRequest meetingRequest);
    public ResponseEntity<MeetingResponse> getMeetingById(Long id);
}
