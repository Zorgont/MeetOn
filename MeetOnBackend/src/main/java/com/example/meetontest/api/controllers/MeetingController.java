package com.example.meetontest.api.controllers;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import com.example.meetontest.api.payload.response.MeetingResponse;
import com.example.meetontest.api.payload.response.MessageResponse;
import com.example.meetontest.api.repositories.MeetingRepository;
import com.example.meetontest.api.exceptions.ResourceNotFoundException;
import com.example.meetontest.api.repositories.TagRepository;
import com.example.meetontest.api.repositories.UserRepository;
import com.example.meetontest.api.security.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
public class MeetingController {
    @Autowired
    MeetingService meetingService;
    @GetMapping
    public Iterable<MeetingResponse> getMeetings(@RequestParam @Nullable List<String> tags) {
        return meetingService.getMeetingsByTags(tags);
    }

    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody CreateMeetingRequest meetingRequest) {
       return  meetingService.createMeeting(meetingRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> getMeetingById(@PathVariable Long id) {
        return meetingService.getMeetingById(id);
    }

    /*@PutMapping("/{id}")
    //todo:Реализовать методы изменения и удаления митингов на фронте
    public ResponseEntity<Meeting> updateMeeting(@PathVariable Long id, @RequestBody Meeting newMeeting) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meeting no exist!"));
        meeting.setName(newMeeting.getFirstName());
        user.setAbout(newUser.getAbout());
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User no exist!"));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }*/

}