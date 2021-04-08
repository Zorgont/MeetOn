package com.example.meetontest.services;

import com.example.meetontest.entities.*;
import java.util.List;

public interface CommentService {
    Comment create(Comment comment);
    List<Comment> getByUser(User user);
    List<Comment> getByMeeting(Meeting meeting);
    void removeById(Long id);
}
