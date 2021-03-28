package com.example.meetontest.api.services.impl;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.exceptions.ValidatorException;
import com.example.meetontest.api.services.MeetingValidator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MeetingValidatorImpl implements MeetingValidator {


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
