package com.uny.unydatabaseredmine.services;

import com.uny.unydatabaseredmine.models.Comment;
import com.uny.unydatabaseredmine.repos.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;


    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
