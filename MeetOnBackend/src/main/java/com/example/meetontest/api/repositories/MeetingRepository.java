package com.example.meetontest.api.repositories;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByManager(User manager);
    List<Meeting> findByTags(Tag tag);
}
