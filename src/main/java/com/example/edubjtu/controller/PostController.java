package com.example.edubjtu.controller;

import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.PostService;
import com.example.edubjtu.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private StudentService studentService;


    @GetMapping("/getPosts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listPostsByStudent(HttpSession session, @RequestParam("studentNum") String studentNum) {
        Student student = studentService.getStudentByStudentNum(studentNum);

        List<Post> posts = postService.getPostsByStudentId(student.getId());
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("posts", posts);
        int postsNum = posts.size(); // 获取通知的数量
        modelMap.put("postNum", postsNum);
        return ResponseEntity.ok(modelMap);
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "postDetails";
    }
}
