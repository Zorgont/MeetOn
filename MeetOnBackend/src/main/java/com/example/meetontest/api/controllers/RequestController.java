package com.example.meetontest.api.controllers;

import com.example.meetontest.api.dto.RequestDTO;
import com.example.meetontest.api.entities.Request;
import com.example.meetontest.api.dto.MessageResponse;
import com.example.meetontest.api.services.MeetingService;
import com.example.meetontest.api.services.RequestConverter;
import com.example.meetontest.api.services.RequestService;
import com.example.meetontest.api.services.UserAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/requests")
@RequiredArgsConstructor
public class RequestController {
    private final MeetingService meetingService;
    private final UserAPIService userAPIService;
    private final RequestService requestService;
    private final RequestConverter requestConverter;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        Optional<Request> request = requestService.getById(id);
        return request.isPresent() ? ResponseEntity.ok(request.get()) :
                ResponseEntity.badRequest().body(new MessageResponse("Request not found!"));
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody RequestDTO requestDTO) {
        return ResponseEntity.ok(requestService.create(requestConverter.convert(requestDTO)));
    }

    @GetMapping("/byUser/{id}")
    public List<Request> getRequestsByUserId(@PathVariable Long userId) {
        return requestService.getByUser(userAPIService.getUserById(userId));
    }

    @GetMapping("/byMeeting/{id}")
    public List<Request> getRequestsByMeetingId(@PathVariable Long meetingId){
        return null;
//        requestService.getByMeeting(meetingService.getMeetingById(meetingId).getBody());
    }
}
