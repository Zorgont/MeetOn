package com.example.meetontest.api.converters;

import com.example.meetontest.api.dto.RequestDTO;
import com.example.meetontest.api.entities.Request;
import com.example.meetontest.api.repositories.MeetingRepository;
import com.example.meetontest.api.services.UserAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestConverter implements Converter<Request, RequestDTO> {

    private final UserAPIService userAPIService;
    private final MeetingRepository meetingService;
    @Override
    public Request convert(RequestDTO entity) {
        Request request=new Request();
        request.setAbout(entity.getAbout());
        request.setMeeting(meetingService.findById(entity.getMeeting_id()).get());
        request.setUser(userAPIService.getUserById(entity.getUser_id()));
        return request;
    }

    @Override
    public RequestDTO convertBack(Request entity) {
        return new RequestDTO(entity.getRequest_id(),entity.getAbout(),entity.getMeeting().getId(),entity.getUser().getId(),entity.getStatus().toString());
    }
}
