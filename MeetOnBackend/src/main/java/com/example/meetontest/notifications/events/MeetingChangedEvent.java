package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.MeetingDTO;
import java.util.Date;

public class MeetingChangedEvent  extends NotificationAbstractEvent<MeetingDTO> {
    public MeetingChangedEvent(Object source, Date date, MeetingDTO oldValue, MeetingDTO newValue) {
        super(source, date, oldValue, newValue);
    }
}
