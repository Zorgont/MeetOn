package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import com.example.meetontest.api.payload.response.MessageResponse;
import com.example.meetontest.api.repositories.TagRepository;
import com.example.meetontest.api.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeetingValidatorImpl implements MeetingValidator{

    @Override
    public ResponseEntity<?> validate(CreateMeetingRequest meetingRequest, Set<Tag> tags , Optional<User> manager) {
        if (!manager.isPresent())
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Wrong meeting manager username!"));

        // Валидация даты - проверка на то, что указанная дата в будущем:
        // todo: Добавить проверку after, чтобы нельзя было запланировать собрание через 100 лет:
        if (meetingRequest.getDate().before(new Date()))
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Wrong date of the meeting!"));

        // Валидация тегов - они существуют в БД и их количество не ноль:

        if (tags.isEmpty())
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Meeting must have at least 1 tag!"));

        return null;
    }
}
