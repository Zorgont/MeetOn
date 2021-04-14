package com.example.meetontest.notifications.events;

import com.example.meetontest.dto.MeetingDTO;
import java.util.Date;

public class MeetingChangedEvent  extends NotificationAbstractEvent<MeetingDTO> {
    public MeetingChangedEvent(Object source, MeetingDTO oldValue, MeetingDTO newValue, Date date) {
        super(source, date, oldValue, newValue);
    }
}
