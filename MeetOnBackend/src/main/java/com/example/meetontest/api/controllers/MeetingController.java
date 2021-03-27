package com.example.meetontest.api.controllers;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import com.example.meetontest.api.payload.response.MeetingResponse;
import com.example.meetontest.api.security.services.MeetingService;
import com.example.meetontest.api.security.services.MeetingServiceImpl;
import com.example.meetontest.api.security.services.UserAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final UserAPIService userAPIService;
    @GetMapping
    public Iterable<MeetingResponse> getMeetings(@RequestParam @Nullable List<String> tags) {
        return meetingService.getMeetingsByTags(tags);
    }
    @GetMapping("/byManager/{name}")
    public Iterable<MeetingResponse> getMeetingsByManager(@PathVariable String name) {
        return meetingService.getMeetingsByManager(userAPIService.getUserByName(name));
    }
    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody CreateMeetingRequest meetingRequest) {
       return  meetingService.createMeeting(meetingRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> getMeetingById(@PathVariable Long id) {
        return meetingService.getMeetingById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return meetingService.deleteMeeting(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting (@PathVariable Long id, @RequestBody CreateMeetingRequest meetingRequest) {
        return meetingService.updateMeeting(id,meetingRequest);
    }


}