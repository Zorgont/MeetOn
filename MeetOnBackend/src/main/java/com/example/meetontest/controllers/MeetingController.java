package com.example.meetontest.controllers;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.converters.MeetingPlatformsConverter;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.dto.MeetingPlatformsDTO;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.dto.NullFieldsErrorResponse;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingPlatform;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.rating.recommendation.MeetingRecommendationsService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.UserService;
import com.example.meetontest.validators.MeetingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final UserService userService;
    private final MeetingConverter meetingConverter;
    private final MeetingPlatformsConverter meetingPlatformsConverter;
    private final MeetingRecommendationsService meetingRecommendationsService;
    private final MeetingValidator meetingValidator;

    @GetMapping
    public Iterable<MeetingDTO> getMeetings(@RequestParam @Nullable List<String> tags, @RequestParam int page ) {
        return  meetingRecommendationsService.getRecommendations(meetingService.getMeetingsByTags(tags),null, page)
                .stream().map(meetingConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/recommended")
    public Iterable<MeetingDTO> getRecommendedMeetings(@RequestParam @Nullable List<String> tags, @RequestParam int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        return meetingRecommendationsService.getRecommendations(meetingService.getMeetingsByTags(tags), userService.getUserByName(currentUserName), page)
                .stream().map(meetingConverter::convertBack).collect(Collectors.toList());//Ограничения по времени создания
    }

    @GetMapping("/pagesNumber")
    public Integer getPagesNumberByTags(@RequestParam @Nullable List<String> tags){
        return meetingService.getMeetingsByTags(tags).size() / 10 + 1;
    }

    @GetMapping("/fields")
    public Iterable<String> getFieldsList() throws IllegalAccessException {
        return meetingValidator.getFieldsList();
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

            List<String> nullFieldsList = meetingValidator.validate(meetingRequest);
            if(!nullFieldsList.isEmpty()) return ResponseEntity.badRequest().body(new NullFieldsErrorResponse("Some required fields are empty!", nullFieldsList));
            if(!meetingValidator.validateUser(meetingRequest, userService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName())))
                throw new ValidatorException("Users doesn't match!");
            Set<MeetingPlatform> meetingPlatforms = new HashSet<>();
            for (MeetingPlatformsDTO meetingPlatformsDTO : meetingRequest.getMeetingPlatforms()) {
                MeetingPlatform convert = meetingPlatformsConverter.convert(meetingPlatformsDTO);
                meetingPlatforms.add(convert);
            }
            Meeting meeting = meetingService.createMeeting(meetingConverter.convert(meetingRequest), userService.getUserByName(meetingRequest.getManagerUsername()), meetingPlatforms);
            return ResponseEntity.ok(meetingConverter.convertBack(meeting));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id, @RequestBody MeetingDTO meetingRequest) throws ValidatorException {
        try {
            Set<MeetingPlatform> meetingPlatforms = new HashSet<>();
            for (MeetingPlatformsDTO meetingPlatformsDTO : meetingRequest.getMeetingPlatforms()) {
                MeetingPlatform convert = meetingPlatformsConverter.convert(meetingPlatformsDTO);
                meetingPlatforms.add(convert);
            }
            return ResponseEntity.ok(meetingService.updateMeeting(id, meetingConverter.convert(meetingRequest), meetingPlatforms));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return meetingService.deleteMeeting(id) ? ResponseEntity.ok("Meeting deleted!") : ResponseEntity.badRequest().body(new MessageResponse("Id not found!"));
    }
}