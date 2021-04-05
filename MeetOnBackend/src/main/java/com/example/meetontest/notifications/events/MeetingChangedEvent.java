package com.example.meetontest.notifications.events;

import com.example.meetontest.entities.Meeting;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MeetingChangedEvent  extends ApplicationEvent {
    private final Meeting oldValue;
    private final Meeting newValue;
    public MeetingChangedEvent(Object source,Meeting oldValue,Meeting newValue) {
        super(source);
        this.oldValue=oldValue;
        this.newValue=newValue;
    }
}
