package com.example.meetontest.api.services.impl;

import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.repositories.TagRepository;
import com.example.meetontest.api.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public List<String> getTags(){
        return tagRepository.findAll().stream().map(tag -> tag.getName()).collect(Collectors.toList());
    }

    @Override
    public Set<Tag> getTags(List<String> tags) {
        return tagRepository.findAll().stream().filter(tag -> tags.contains(tag.getName())).collect(Collectors.toSet());
    }

}
