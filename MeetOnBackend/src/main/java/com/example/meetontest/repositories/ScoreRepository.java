package com.example.meetontest.repositories;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<MeetingScore, Long> {
    List<MeetingScore> findByMeeting(Meeting meeting);
    List<MeetingScore> findByUser(User user);
}
