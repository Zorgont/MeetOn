package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MeetingChangedEvent  extends NotificationAbstractEvent {
    private final MeetingDTO oldValue;
    private final MeetingDTO newValue;

    public MeetingChangedEvent(Object source, MeetingDTO oldValue, MeetingDTO newValue, Date date) {
        super(date);
        this.oldValue=oldValue;
        this.newValue=newValue;
        this.type = EventType.MEETING_CHANGED;
    }
    @Override
    public NotificationEvent toEntity() throws JsonProcessingException {
        Map<String, MeetingDTO> map = new HashMap<>();
        map.put("old", oldValue);
        map.put("new", newValue);
        return new NotificationEvent(this.date, this.type, new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));
    }
}
