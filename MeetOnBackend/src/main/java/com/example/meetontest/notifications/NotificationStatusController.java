package com.example.meetontest.notifications;

import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.notifications.converters.NotificationConverter;
import com.example.meetontest.notifications.dto.NotificationDTO;
import com.example.meetontest.notifications.entities.NotificationStatus;
import com.example.meetontest.notifications.services.NotificationService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import com.example.meetontest.exceptions.ValidatorException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/notifications")
@RequiredArgsConstructor
public class NotificationStatusController {
    private final NotificationService notificationService;
    private final NotificationConverter notificationConverter;
    private final UserService userService;
    @GetMapping("/byUser/{id}")
    public ResponseEntity<?> getByUser(@PathVariable Long id, @RequestParam @Nullable String status){
        try {
            return ResponseEntity.ok(notificationService.getByUserAndStatus(userService.getUserById(id), parseStatus(status)).stream().
                    map(notificationConverter::convertBack).collect(Collectors.toList()));
        }
        catch (ValidatorException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
    private NotificationStatus parseStatus(String status) throws ValidatorException{
        try{
            return status!=null? NotificationStatus.valueOf(status.toUpperCase()):null;

        }
        catch (IllegalArgumentException e){
            throw new ValidatorException("Incorrect status!");
        }
    }
}
