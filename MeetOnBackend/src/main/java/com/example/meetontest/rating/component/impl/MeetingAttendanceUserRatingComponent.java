package com.example.meetontest.rating.component.impl;

import com.example.meetontest.entities.MeetingRole;
import com.example.meetontest.entities.RatingWeightType;
import com.example.meetontest.entities.User;
import com.example.meetontest.rating.component.UserRatingComponent;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RatingWeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingAttendanceUserRatingComponent implements UserRatingComponent {
    private final MeetingService meetingService;
    private final RatingWeightService weightService;

    @Override
    public double getUserRating(User user) {
        try {
            double result = meetingService.getMeetingsByManager(user).stream()
                    .flatMap(meeting -> meeting.getRequests().stream())
                    .filter(request -> request.getRole().equals(MeetingRole.PARTICIPANT))
                    .count() * weightService.getValueByType(RatingWeightType.MEETING_ATTENDANCE);

            return Double.isNaN(result) ? 0 : result;
        } catch (Exception e) {
            System.out.println("Couldn't calculate meeting attendance for userId: " + user.getId());
            return 0;
        }
    }
}
