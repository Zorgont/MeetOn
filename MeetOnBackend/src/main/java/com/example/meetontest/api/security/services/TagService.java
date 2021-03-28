package com.example.meetontest.api.security.services;

import com.example.meetontest.api.entities.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<String> getTags();
    Set<Tag> getTags(List<String> tags);
}
