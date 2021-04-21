package com.example.meetontest.services.impl;

import com.example.meetontest.entities.TagGroup;
import com.example.meetontest.entities.User;
import com.example.meetontest.repositories.TagGroupRepository;
import com.example.meetontest.services.TagGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagGroupServiceImpl implements TagGroupService {
    private final TagGroupRepository tagGroupRepository;

    @Override
    public TagGroup createTagGroup(TagGroup tagGroup, User user) {
        tagGroup.setUser(user);
        return tagGroupRepository.save(tagGroup);
    }

    @Override
    public List<TagGroup> getByUser(User user) {
        return tagGroupRepository.findByUser(user);
    }

    @Override
    public List<TagGroup> getNotifiableByUser(User user) {
        return tagGroupRepository.findByUserAndIsNotifiable(user, true);
    }

    @Override
    public void deleteByGroupId(Long id) {
        tagGroupRepository.deleteById(id);
    }

    @Override
    public TagGroup setNotifiable(Long id, boolean isNotifiable) {
        TagGroup tagGroup = tagGroupRepository.findById(id).get();
        tagGroup.setIsNotifiable(isNotifiable);
        return tagGroupRepository.save(tagGroup);
    }
}
