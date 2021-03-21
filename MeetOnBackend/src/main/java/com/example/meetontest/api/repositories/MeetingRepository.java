package com.example.meetontest.api.repositories;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByManager(User manager);
}
