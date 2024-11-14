package com.example.edubjtu.service;

import com.example.edubjtu.dto.MyComment;
import com.example.edubjtu.dto.PostComment;
import com.example.edubjtu.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<MyComment> getAllCommentsByStudentId(Long studentId) {
        return commentRepository.findCommentsByStudentId(studentId);
    }

    public List<PostComment> getCommentByPostId(Long postId) {
        return commentRepository.findCommentsByPostId(postId);
    }
}
