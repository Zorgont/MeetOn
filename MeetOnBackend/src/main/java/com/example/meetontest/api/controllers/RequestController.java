package com.example.meetontest.api.controllers;

import com.example.meetontest.api.dto.RequestDTO;
import com.example.meetontest.api.entities.Request;
import com.example.meetontest.api.dto.MessageResponse;
import com.example.meetontest.api.entities.RequestStatus;
import com.example.meetontest.api.services.MeetingService;
import com.example.meetontest.api.converters.RequestConverter;
import com.example.meetontest.api.services.RequestService;
import com.example.meetontest.api.services.UserAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        return request.isPresent() ? ResponseEntity.ok(requestConverter.convertBack(request.get())) :
                ResponseEntity.badRequest().body(new MessageResponse("Request not found!"));
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody RequestDTO requestDTO) {
        return ResponseEntity.ok(requestConverter.convertBack(requestService.create(requestConverter.convert(requestDTO))));
    }

    @GetMapping("/byUser/{id}")
    public List<RequestDTO> getRequestsByUserId(@PathVariable Long id) {
        return requestService.getByUser(userAPIService.getUserById(id)).stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/byMeeting/{id}")
    public List<RequestDTO> getRequestsByMeetingId(@PathVariable Long id){
        return requestService.getByMeeting(meetingService.getMeetingById(id)).stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            requestService.changeStatus(requestService.getById(id).get(), RequestStatus.valueOf(status.toUpperCase()));
            return ResponseEntity.ok("Success");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed updating status!"));
        }
    }
    @GetMapping("/check")
    public boolean checkRequestExistence(@RequestParam Long meetingId, @RequestParam Long userId){
        return requestService.existsByMeetingIdUserId(meetingId,userId);
    }
}
