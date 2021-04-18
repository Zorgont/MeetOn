package com.example.meetontest.controllers;

import com.example.meetontest.converters.MeetingPlatformsConverter;
import com.example.meetontest.dto.MeetingPlatformsDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingPlatform;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private static final Logger log = LoggerFactory.getLogger(MeetingController.class);

    private final MeetingService meetingService;
    private final UserService userService;
    private final MeetingConverter meetingConverter;
    private final MeetingPlatformsConverter meetingPlatformsConverter;

    @GetMapping
    public Iterable<MeetingDTO> getMeetings(@RequestParam @Nullable List<String> tags) {
        return meetingService.getMeetingsByTags(tags).stream().map(meetingConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/byManager/{name}")
    public Iterable<MeetingDTO> getMeetingsByManager(@PathVariable String name) {
        return meetingService.getMeetingsByManager(userService.getUserByName(name)).stream().map(meetingConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingConverter.convertBack(meetingService.getMeetingById(id)));
    }

    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody MeetingDTO meetingRequest) throws ValidatorException {
        try {
            Set<MeetingPlatform> meetingPlatforms = new HashSet<>();
            for (MeetingPlatformsDTO meetingPlatformsDTO : meetingRequest.getMeetingPlatforms()) {
                MeetingPlatform convert = meetingPlatformsConverter.convert(meetingPlatformsDTO);
                meetingPlatforms.add(convert);
            }
            Meeting meeting = meetingService.createMeeting(meetingConverter.convert(meetingRequest), userService.getUserByName(meetingRequest.getManagerUsername()), meetingPlatforms);
            return ResponseEntity.ok(meetingConverter.convertBack(meeting));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id, @RequestBody MeetingDTO meetingRequest) throws ValidatorException, ParseException {
        try {
            Set<MeetingPlatform> meetingPlatforms = new HashSet<>();
            for (MeetingPlatformsDTO meetingPlatformsDTO : meetingRequest.getMeetingPlatforms()) {
                MeetingPlatform convert = meetingPlatformsConverter.convert(meetingPlatformsDTO);
                meetingPlatforms.add(convert);
            }
            return ResponseEntity.ok(meetingService.updateMeeting(id, meetingConverter.convert(meetingRequest), meetingPlatforms));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return meetingService.deleteMeeting(id) ? ResponseEntity.ok("Meeting deleted!") : ResponseEntity.badRequest().body(new MessageResponse("Id not found!"));
    }
}