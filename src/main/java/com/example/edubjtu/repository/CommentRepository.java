package com.example.edubjtu.repository;

import com.example.edubjtu.dto.MyComment;
import com.example.edubjtu.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    // 根据 studentId 获取所有的 MyComment DTO
    @Query("""
            SELECT new com.example.edubjtu.dto.MyComment(c.commentId, c.postId, c.likeNum, c.content, \
            c.commentedNum, s.name, c.teacherId, c.studentId, p.title) \
            FROM Comment c \
            JOIN Post p ON c.postId = p.postId \
            JOIN Student s on  s.studentNum= c.commentedNum \
            WHERE c.studentId = :student_id""")
    List<MyComment> findCommentsByStudentId(@Param("student_id") Long studentId);}
