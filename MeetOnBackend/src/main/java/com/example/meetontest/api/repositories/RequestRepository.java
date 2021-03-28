package com.example.meetontest.api.repositories;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Request;
import com.example.meetontest.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUser(User user);
    List<Request> findByMeeting(Meeting meeting);
    boolean existsByMeetingAndUser(Meeting meeting,User user);
}
