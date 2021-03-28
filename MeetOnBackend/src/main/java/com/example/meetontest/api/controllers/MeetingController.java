package com.example.meetontest.api.controllers;

import com.example.meetontest.api.exceptions.ValidatorException;
import com.example.meetontest.api.dto.MeetingDTO;
import com.example.meetontest.api.dto.MessageResponse;
import com.example.meetontest.api.converters.MeetingConverter;
import com.example.meetontest.api.services.MeetingService;
import com.example.meetontest.api.services.UserAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final UserAPIService userAPIService;
    private final MeetingConverter meetingConverter;
    @GetMapping
    public Iterable<MeetingDTO> getMeetings(@RequestParam @Nullable List<String> tags) {
        return meetingService.getMeetingsByTags(tags).stream().map(meetingConverter::convertBack).collect(Collectors.toList());
    }
    @GetMapping("/byManager/{name}")
    public Iterable<MeetingDTO> getMeetingsByManager(@PathVariable String name) {
        return meetingService.getMeetingsByManager(userAPIService.getUserByName(name)).stream().map(meetingConverter::convertBack).collect(Collectors.toList());
    }
    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody MeetingDTO meetingRequest) throws ValidatorException {
        try {
            return ResponseEntity.ok(meetingService.createMeeting(meetingConverter.convert(meetingRequest)));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingConverter.convertBack(meetingService.getMeetingById(id)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return meetingService.deleteMeeting(id) ? ResponseEntity.ok("Meeting deleted!") : ResponseEntity.badRequest().body(new MessageResponse("Id not found!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting (@PathVariable Long id, @RequestBody MeetingDTO meetingRequest) throws ValidatorException {
        try {
            return ResponseEntity.ok(meetingService.updateMeeting(id,meetingConverter.convert(meetingRequest)));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }


}