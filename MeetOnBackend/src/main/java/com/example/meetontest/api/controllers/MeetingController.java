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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
public class MeetingController {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public Iterable<MeetingResponse> getMeetings() {
        return meetingRepository.findAll().stream().map(MeetingResponse::new).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody CreateMeetingRequest meetingRequest) {
        // Валидация менеджера - проверка на его существование как пользователя в БД:
        Optional<User> manager = userRepository.findByUsername(meetingRequest.getManagerUsername());
        if (!manager.isPresent())
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Wrong meeting manager username!"));

        // Валидация даты - проверка на то, что указанная дата в будущем:
        // todo: Добавить проверку after, чтобы нельзя было запланировать собрание через 100 лет:
        if (meetingRequest.getDate().before(new Date()))
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Wrong date of the meeting!"));

        // Валидация тегов - они существуют в БД и их количество не ноль:
        Set<Tag> tags = tagRepository.findAll().stream()
                .filter(tag -> meetingRequest.getTags().contains(tag.getName()))
                .collect(Collectors.toSet());
        if (tags.isEmpty())
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Meeting must have at least 1 tag!"));

        Meeting meeting = new Meeting(
                meetingRequest.getName(),
                meetingRequest.getDate(),
                meetingRequest.getAbout(),
                meetingRequest.isParticipantAmountRestricted(),
                meetingRequest.getParticipantAmount(),
                meetingRequest.isPrivate(),
                meetingRequest.getDetails(),
                "Planning",
                manager.get(),
                tags);
        meetingRepository.save(meeting);

        return ResponseEntity.ok(new MeetingResponse(meeting));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> getMeetingById(@PathVariable Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meeting no exist!"));
        return ResponseEntity.ok(new MeetingResponse(meeting));
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