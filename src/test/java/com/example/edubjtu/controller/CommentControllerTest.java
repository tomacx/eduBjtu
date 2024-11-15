package com.example.edubjtu.controller;

import com.example.edubjtu.dto.MyComment;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.CommentService;
import com.example.edubjtu.service.StudentService;
import com.example.edubjtu.service.TeacherService;
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
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private TeacherService teacherService;

    @Test
    public void testGetComments() throws Exception {
        Student student = new Student();
        student.setId(1L);
        List<MyComment> commentList = new ArrayList<>();
        Mockito.when(studentService.findStudentByStudentNum("123")).thenReturn(student);
        Mockito.when(commentService.getAllCommentsByStudentId(1L)).thenReturn(commentList);

        mockMvc.perform(MockMvcRequestBuilders.get("/comment/getComments")
                .param("userNum", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentList").exists());
    }
} 