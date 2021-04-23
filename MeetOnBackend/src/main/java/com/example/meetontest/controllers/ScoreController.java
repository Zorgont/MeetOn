package com.example.meetontest.controllers;

import com.example.meetontest.converters.ScoreConverter;
import com.example.meetontest.dto.AggregatedScoreDTO;
import com.example.meetontest.dto.ScoreDTO;
import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.ScoreService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;
    private final UserService userService;
    private final MeetingService meetingService;
    private final ScoreConverter converter;

    @GetMapping("/{meetingId}/aggregated")
    public AggregatedScoreDTO getAggregatedScoreByMeeting(@PathVariable Long meetingId) {
        return scoreService.getAggregatedScoreByMeeting(meetingService.getMeetingById(meetingId));
    }

    @GetMapping("/{meetingId}/byUser/{userId}")
    public ScoreDTO getScoreByMeetingAndUser(@PathVariable Long meetingId, @PathVariable Long userId) {
        return converter.convertBack(scoreService.getByMeetingAndUser(meetingService.getMeetingById(meetingId), userService.getUserById(userId)));
    }

    @PostMapping("/{meetingId}")
    public ScoreDTO createScore(@PathVariable Long meetingId, @RequestBody ScoreDTO score) throws ParseException {
        score.setMeeting_id(meetingId);
        MeetingScore meetingScore = converter.convert(score);
        return converter.convertBack(scoreService.createScore(meetingScore.getMeeting(), meetingScore.getUser(), meetingScore.getScore()));
    }
}
