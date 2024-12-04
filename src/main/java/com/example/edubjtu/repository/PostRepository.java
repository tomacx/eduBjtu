package com.example.edubjtu.repository;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.Post;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            Select new com.example.edubjtu.dto.CoursePost( 
                p.postId, 
                p.courseId,  
                s.name,  
                p.likeNum, 
                p.favoNum, 
                p.content,  
                p.title 
            ) 
            from Post p 
            LEFT JOIN Student s on s.id = p.studentId 
            where p.courseId = :courseId
""")
    List<CoursePost> findByCourseId(Long courseId);


    List<Post> findByStudentId(Long studentId);

    @Query(value = """
            Select new com.example.edubjtu.dto.CoursePost( p.postId, p.courseId,  s.name,  p.likeNum, p.favoNum, p.content,  p.title ) from Post p \
             JOIN Student s on s.id =  p.studentId where p.postId=:postId""")
    List<CoursePost> findCoursePostById(@Param("postId") Long postId);

    //搜索帖子
    List<Post> findByTitleContaining(String title);

    @Transactional
    @Modifying
    @Query("DELETE from Post p WHERE p.postId = :postId" )
    void deletePostByPostId(@Param("postId") Long postId);

    // 自定义查询方法来增加帖子的点赞数
    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.likeNum = p.likeNum + 1 WHERE p.postId = :postId")
    void incrementLikes(@Param("postId") Long postId);

    Post findByPostId(Long postId);
}
