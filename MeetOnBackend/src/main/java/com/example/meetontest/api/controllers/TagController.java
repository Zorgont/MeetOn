package com.example.meetontest.api.controllers;


import com.example.meetontest.api.security.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "api/v1/tags")
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping
    List<String> getTags(){
        return tagService.getTags();
    }
}
