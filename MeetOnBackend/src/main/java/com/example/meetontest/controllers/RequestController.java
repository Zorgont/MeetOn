package com.example.meetontest.controllers;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.Request;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.converters.RequestConverter;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/requests")
@RequiredArgsConstructor
public class RequestController {
    private final MeetingService meetingService;
    private final UserService userService;
    private final RequestService requestService;
    private final RequestConverter requestConverter;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        Optional<Request> request = requestService.getById(id);
        return request.isPresent() ? ResponseEntity.ok(requestConverter.convertBack(request.get())) :
                ResponseEntity.badRequest().body(new MessageResponse("Request not found!"));
    }

    @GetMapping("/byUser/{id}")
    public List<RequestDTO> getRequestsByUserId(@PathVariable Long id) {
        return requestService.getByUser(userService.getUserById(id)).stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/byMeeting/{id}")
    public List<RequestDTO> getRequestsByMeetingId(@PathVariable Long id){
        return requestService.getByMeeting(meetingService.getMeetingById(id)).stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/by")
    public RequestDTO getRequestByMeetingAndUser(@RequestParam Long meetingId, @RequestParam Long userId){
        return requestService.getByMeetingIdUserId(meetingId, userId).map(requestConverter::convertBack).orElse(null);
    }

    @GetMapping("/amount/{id}")
    public int getRequestAmountByMeetingId(@PathVariable Long id) {
        return requestService.getApprovedRequestsAmount(id);
    }
    @GetMapping("/pendingRequests/{id}")
    public List<RequestDTO> getPendingRequests(@PathVariable Long id){
        return requestService.getByMeetingAndStatus(meetingService.getMeetingById(id),RequestStatus.PENDING).
                stream().map(requestConverter::convertBack).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody RequestDTO requestDTO) {
        return ResponseEntity.ok(requestConverter.convertBack(requestService.create(requestConverter.convert(requestDTO))));
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            requestService.changeStatus(requestService.getById(id).get(), RequestStatus.valueOf(status.toUpperCase()));
            return ResponseEntity.ok(requestConverter.convertBack(requestService.getById(id).get()));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed updating status!"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeRequestById(@PathVariable Long id) {
        try {
            requestService.removeById(id);
            return ResponseEntity.ok("deleted");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}