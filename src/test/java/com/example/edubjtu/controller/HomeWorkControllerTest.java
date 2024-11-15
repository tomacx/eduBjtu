package com.example.edubjtu.controller;

import com.example.edubjtu.model.Homework;
import com.example.edubjtu.service.HomeWorkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeWorkController.class)
public class HomeWorkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeWorkService homeWorkService;

    @Test
    public void testHomeworks() throws Exception {
        Mockito.when(homeWorkService.getHomeworkByCourseId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/homeworks")
                .param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeworkList").exists());
    }

    @Test
    public void testHomework() throws Exception {
        Homework homework = new Homework();
        Mockito.when(homeWorkService.getHomeworkByHomeworkId(1L)).thenReturn(homework);

        mockMvc.perform(MockMvcRequestBuilders.get("/homework")
                .param("homeworkId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homework").exists());
    }
} 