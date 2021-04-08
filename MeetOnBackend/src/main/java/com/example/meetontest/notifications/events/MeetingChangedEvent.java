package com.example.meetontest.notifications.events;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.notifications.entities.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MeetingChangedEvent  extends NotificationAbstractEvent {
    private final Meeting oldValue;
    private final Meeting newValue;

    public MeetingChangedEvent(Object source, Meeting oldValue, Meeting newValue, Date date) {
        super(source,date);
        this.oldValue=oldValue;
        this.newValue=newValue;
        this.type="MEETING_CHANGED";
    }
    @Override
    public NotificationEvent toEntity() throws JsonProcessingException {
        Map<String,Meeting> map=new HashMap<>();
        map.put("old",oldValue);
        map.put("new",newValue);
        return new NotificationEvent(this.date,this.type,new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));
    }
}
