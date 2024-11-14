package com.example.edubjtu.controller;

import com.example.edubjtu.service.AiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AiController.class)
public class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiService aiService;

    @Test
    public void testCourseAi() throws Exception {
        String question = "What is AI?";
        String answer = "AI is artificial intelligence.";
        Mockito.when(aiService.getAnswerOfAi(question)).thenReturn(answer);

        mockMvc.perform(MockMvcRequestBuilders.get("/ai/courseAi")
                .param("question", question))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value(answer));
    }
} 