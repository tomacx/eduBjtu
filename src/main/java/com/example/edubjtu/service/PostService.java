package com.example.edubjtu.service;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<CoursePost> getPostsByCourseId(Long courseId) {
        return postRepository.findByCourseId(courseId);
    }

    public List<Post> getPostsByStudentId(Long studentId) {
        return postRepository.findByStudentId(studentId);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}
