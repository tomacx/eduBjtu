package com.example.edubjtu.service;

import com.example.edubjtu.dto.MyComment;
import com.example.edubjtu.dto.PostComment;
import com.example.edubjtu.model.Comment;
import com.example.edubjtu.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentService {
    //TODO:增加函数
    @Autowired
    private CommentRepository commentRepository;

    public List<MyComment> getAllCommentsByStudentId(Long studentId) {
        return commentRepository.findCommentsByStudentId(studentId);
    }

    public List<PostComment> getCommentByPostId(Long postId) {
        return commentRepository.findCommentsByPostId(postId);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findByCommentId(commentId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteByCommentId(commentId);
    }

    public void setCommentByStudent(Long postId, String content, String commentedNum, Long id) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setCommentedNum(commentedNum);
        comment.setLikeNum(0);
        comment.setStudentId(id);
        commentRepository.save(comment);
    }
    public  void  setCommentByTeacher(Long postId, String content, String commentedNum, Long id) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setCommentedNum(commentedNum);
        comment.setLikeNum(0);
        comment.setTeacherId(id);
        commentRepository.save(comment);
    }
}
