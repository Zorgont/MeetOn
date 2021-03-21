package com.example.meetontest.api.controllers;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.repositories.MeetingRepository;
import com.example.meetontest.api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
public class MeetingController {
    @Autowired
    private MeetingRepository meetingRepository;

    @GetMapping
    public Iterable<Meeting> getMeetings() {
        return meetingRepository.findAll();
    }

    @PostMapping
    public Meeting createMeeting(@RequestParam Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meeting no exist!"));
        return ResponseEntity.ok(meeting);
    }

    /*@PutMapping("/{id}")
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