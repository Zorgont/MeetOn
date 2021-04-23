package com.example.meetontest.converters;

import com.example.meetontest.dto.ScoreDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.entities.User;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class ScoreConverter implements Converter<MeetingScore, ScoreDTO> {

    @Override
    public MeetingScore convert(ScoreDTO entity) throws ParseException {
        MeetingScore score = new MeetingScore();
        score.setScore_id(entity.getId());
        score.setScore(entity.getScore());
        score.setDate(entity.getDate());
        Meeting meeting = new Meeting();
        meeting.setId(entity.getId());
        score.setMeeting(meeting);
        User user = new User();
        user.setId(entity.getId());
        score.setUser(user);
        return score;
    }

    @Override
    public ScoreDTO convertBack(MeetingScore entity) {
        return new ScoreDTO(entity.getScore_id(), entity.getMeeting().getId(), entity.getUser().getId(), entity.getScore(), entity.getDate());
    }
}
