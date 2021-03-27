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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService{

    private final MeetingRepository meetingRepository;


    private final UserRepository userRepository;


    private final TagRepository tagRepository;


    private final MeetingValidator meetingValidator;

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
        Set<Tag> tags = tagRepository.findAll().stream()
                .filter(tag -> meetingRequest.getTags().contains(tag.getName()))
                .collect(Collectors.toSet());

        ResponseEntity<?> validationResponse=meetingValidator.validate(meetingRequest,tags,manager);
        if (validationResponse!=null){
            return validationResponse;
        }

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
