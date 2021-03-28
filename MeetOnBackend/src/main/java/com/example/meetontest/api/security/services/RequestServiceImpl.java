package com.example.meetontest.api.security.services;


import com.example.meetontest.api.entities.Meeting;
import com.example.meetontest.api.entities.Request;
import com.example.meetontest.api.entities.User;
import com.example.meetontest.api.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final RequestRepository requestRepository;


    @Override
    public Request create(Request request) {

        return requestRepository.save(request);
    }

    @Override
    public Optional<Request> getById(Long id) {

        return requestRepository.findById(id);
    }

    @Override
    public List<Request> getByUser(User user) {
        return requestRepository.findByUser(user);
    }

    @Override
    public List<Request> getByMeeting(Meeting meeting) {
        return requestRepository.findByMeeting(meeting);
    }
}
