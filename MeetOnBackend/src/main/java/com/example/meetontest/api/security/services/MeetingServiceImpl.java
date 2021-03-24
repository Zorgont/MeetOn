package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.exceptions.ResourceNotFoundException;
import com.example.meetontest.api.payload.request.CreateMeetingRequest;
import com.example.meetontest.api.payload.response.MeetingResponse;
import com.example.meetontest.api.payload.response.MessageResponse;
import com.example.meetontest.api.repositories.MeetingRepository;
import com.example.meetontest.api.repositories.TagRepository;
import com.example.meetontest.api.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class MeetingServiceImpl implements MeetingService{
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;


    public List<MeetingResponse> getMeetingsByTags(List<String> tags) {
        if (tags == null || tags.isEmpty())
            return meetingRepository.findAll().stream().map(MeetingResponse::new).collect(Collectors.toList());

        Set<Tag> tagsSet = tagRepository.findAll().stream()
                .filter(tag -> tags.contains(tag.getName()))
                .collect(Collectors.toSet());
        Tag first=tagsSet.iterator().next();
        List<Meeting> filteredMeetings=meetingRepository.findByTags(first);
        tagsSet.remove(first);
        for(Tag item:tagsSet){
            filteredMeetings.retainAll(meetingRepository.findByTags(item));
        }

        return filteredMeetings.stream().map(MeetingResponse::new).collect(Collectors.toList());
    }


    public ResponseEntity<?> createMeeting(CreateMeetingRequest meetingRequest) {
        // Валидация менеджера - проверка на его существование как пользователя в БД:
        Optional<User> manager = userRepository.findByUsername(meetingRequest.getManagerUsername());
        if (!manager.isPresent())
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Wrong meeting manager username!"));

        // Валидация даты - проверка на то, что указанная дата в будущем:
        // todo: Добавить проверку after, чтобы нельзя было запланировать собрание через 100 лет:
        if (meetingRequest.getDate().before(new Date()))
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Wrong date of the meeting!"));

        // Валидация тегов - они существуют в БД и их количество не ноль:
        Set<Tag> tags = tagRepository.findAll().stream()
                .filter(tag -> meetingRequest.getTags().contains(tag.getName()))
                .collect(Collectors.toSet());
        if (tags.isEmpty())
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Meeting must have at least 1 tag!"));

        Meeting meeting = new Meeting(
                meetingRequest.getName(),
                meetingRequest.getDate(),
                meetingRequest.getAbout(),
                meetingRequest.getIsParticipantAmountRestricted() == 1,
                meetingRequest.getParticipantAmount(),
                meetingRequest.getIsPrivate() == 1,
                meetingRequest.getDetails(),
                "Planning",
                manager.get(),
                tags);
        meetingRepository.save(meeting);

        return ResponseEntity.ok(new MeetingResponse(meeting));
    }


    public ResponseEntity<MeetingResponse> getMeetingById(Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meeting no exist!"));
        return ResponseEntity.ok(new MeetingResponse(meeting));
    }
}
