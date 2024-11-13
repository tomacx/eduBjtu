package com.example.edubjtu.repository;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            Select new com.example.edubjtu.dto.CoursePost( p.postId, p.courseId,  s.name,  p.likeNum, p.favoNum, p.content,  p.title ) from Post p \
             JOIN Student s on s.id =  p.studentId where p.courseId=:courseId""")
    List<CoursePost> findByCourseId(Long courseId);

    List<Post> findByStudentId(Long studentId);
}
