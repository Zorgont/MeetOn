package com.example.meetontest.services.impl;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.services.MeetingValidator;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MeetingValidatorImpl implements MeetingValidator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    @Override
    public void validate(Meeting meeting) throws ValidatorException {
        if(meeting.getStatus().equals(MeetingStatus.PLANNING)){
            if (meeting.getManager() == null)
                throw new ValidatorException("Incorrect manager!");

            // Валидация даты - проверка на то, что указанная дата в будущем:
            // todo: Добавить проверку after, чтобы нельзя было запланировать собрание через 100 лет:
            if (meeting.getDate().before(new Date()))
                throw new ValidatorException("Incorrect date!");

            if (meeting.getDate().after(meeting.getEndDate()))
                throw new ValidatorException("Date of the meeting cannot be before end date!");

            // Валидация тегов - они существуют в БД и их количество не ноль:
            if (meeting.getTags().isEmpty())
                throw new ValidatorException("Incorrect tags!");
        }
    }
}