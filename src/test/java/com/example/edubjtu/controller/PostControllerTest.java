package com.example.edubjtu.controller;

import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.PostService;
import com.example.edubjtu.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private StudentService studentService;

    @Test
    public void testListPostsByStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(studentService.getStudentByStudentNum("123")).thenReturn(student);
        Mockito.when(postService.getPostsByStudentId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/post/getPosts")
                .param("studentNum", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").exists());
    }

    @Test
    public void testViewPost() throws Exception {
        Post post = new Post();
        Mockito.when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(MockMvcRequestBuilders.get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"));
    }

    @Test
    public void testSearchPosts() throws Exception {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post( 1L ,1L ,1L,1L,0L,0L,"Test Content","Test Title"));
        Mockito.when(postService.searchPostsByTitle("Test")).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders.get("/post/search")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").exists())
                .andExpect(jsonPath("$.postNum").value(1));
    }
} 