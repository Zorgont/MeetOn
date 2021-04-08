package com.example.meetontest.repositories;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.Request;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUser(User user);
    List<Request> findByMeeting(Meeting meeting);
    Optional<Request> findByMeetingAndUser(Meeting meeting, User user);
    int countByMeetingAndStatus(Meeting meeting, RequestStatus status);
    List<Request> findByMeetingAndStatus(Meeting meeting,RequestStatus status);
}