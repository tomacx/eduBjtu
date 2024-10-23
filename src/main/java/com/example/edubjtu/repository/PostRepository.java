package com.example.edubjtu.repository;

import com.example.edubjtu.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCourseId(Long courseId);
    List<Post> findByStudentId(Long studentId);
}
