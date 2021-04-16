package com.example.meetontest.controllers;


import com.example.meetontest.converters.CommentConverter;
import com.example.meetontest.dto.CommentDTO;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.services.CommentService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final MeetingService meetingService;
    private final CommentConverter commentConverter;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/createComment")
    public void createComment(@Payload CommentDTO commentDTO) throws ParseException {



            simpMessagingTemplate.convertAndSend("/meeting/" + commentDTO.getMeeting_id() + "/queue/comments",
                    commentConverter.convertBack(commentService.create(commentConverter.convert(commentDTO))));

    }
    @GetMapping("/byUser/{id}")
    public List<CommentDTO> getCommentsByUserId(@PathVariable Long id){
        return commentService.getByUser(userService.getUserById(id)).stream().
                map(commentConverter::convertBack).collect(Collectors.toList());
    }
    @GetMapping("/byMeeting/{id}")
    public List<CommentDTO> getCommentsByMeeting(@PathVariable Long id){
        return commentService.getByMeeting(meetingService.getMeetingById(id)).stream().
                map(commentConverter::convertBack).collect(Collectors.toList());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCommentById(@PathVariable Long id){
        try {
            commentService.removeById(id);
            return ResponseEntity.ok("deleted!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
