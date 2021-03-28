package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Tag;
import com.example.meetontest.api.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    public List<String> getTags(){
        return tagRepository.findAll().stream().map(tag -> tag.getName()).collect(Collectors.toList());
    }

    @Override
    public Set<Tag> getTags(List<String> tags) {
        return tagRepository.findAll().stream().filter(tag -> tags.contains(tag.getName())).collect(Collectors.toSet());
    }

}
