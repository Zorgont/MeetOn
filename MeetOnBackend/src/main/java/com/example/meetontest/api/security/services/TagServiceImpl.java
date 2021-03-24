package com.example.meetontest.api.security.services;

import com.example.meetontest.api.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class TagServiceImpl implements TagService{
    @Autowired
    TagRepository tagRepository;

    public List<String> getTags(){
        return tagRepository.findAll().stream().map(tag -> tag.getName()).collect(Collectors.toList());
    }
}
