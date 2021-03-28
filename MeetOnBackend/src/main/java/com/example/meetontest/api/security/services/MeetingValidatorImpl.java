package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.exceptions.ValidatorException;
import com.example.meetontest.api.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Component
public class MeetingValidatorImpl implements MeetingValidator{


    @Override
    public void validate(Meeting meeting) throws ValidatorException {
        if (meeting.getManager()==null)
            throw new ValidatorException("Incorrect manager!");

        // Валидация даты - проверка на то, что указанная дата в будущем:
        // todo: Добавить проверку after, чтобы нельзя было запланировать собрание через 100 лет:
        if (meeting.getDate().before(new Date()))
            throw new ValidatorException("Incorrect date!");

        // Валидация тегов - они существуют в БД и их количество не ноль:

        if (meeting.getTags().isEmpty())
            throw new ValidatorException("Incorrect tags!");;
    }
}
