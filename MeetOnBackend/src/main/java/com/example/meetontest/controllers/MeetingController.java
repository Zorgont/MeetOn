package com.example.meetontest.controllers;

import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final UserService userService;
    private final MeetingConverter meetingConverter;

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
    public ResponseEntity<?> createMeeting(@RequestBody MeetingDTO meetingRequest) throws ValidatorException,ParseException {
        try {
            return ResponseEntity.ok(meetingService.createMeeting(meetingConverter.convert(meetingRequest)));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting (@PathVariable Long id, @RequestBody MeetingDTO meetingRequest) throws ValidatorException, ParseException {
        try {
            return ResponseEntity.ok(meetingService.updateMeeting(id,meetingConverter.convert(meetingRequest)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return meetingService.deleteMeeting(id) ? ResponseEntity.ok("Meeting deleted!") : ResponseEntity.badRequest().body(new MessageResponse("Id not found!"));
    }
}