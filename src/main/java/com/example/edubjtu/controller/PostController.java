package com.example.edubjtu.controller;

import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/course/{courseId}")
    public String listPostsByCourse(@PathVariable Long courseId, Model model) {
        List<Post> posts = postService.getPostsByCourseId(courseId);
        model.addAttribute("posts", posts);
        return "postList";
    }

    @GetMapping("/student")
    public String listPostsByStudent(HttpSession session, Model model) {
        Long studentId = ((Student) session.getAttribute("loggedInStudent")).getId();
        List<Post> posts = postService.getPostsByStudentId(studentId);
        model.addAttribute("posts", posts);
        return "studentPosts"; // 确保有studentPosts.html
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "postDetails";
    }
}
