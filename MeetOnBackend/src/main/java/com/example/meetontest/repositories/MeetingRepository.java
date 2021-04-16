package com.example.meetontest.repositories;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.Tag;
import com.example.meetontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByTags(Tag tag);
}