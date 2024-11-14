package com.example.edubjtu.controller;

import com.example.edubjtu.dto.MyFavouritePosts;
import com.example.edubjtu.dto.MyFavoOthersFavo;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.FavoriteService;
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

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FavoriteController.class)
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private StudentService studentService;

    @Test
    public void testListFavorites() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(studentService.findStudentByStudentNum("123")).thenReturn(student);
        Mockito.when(favoriteService.getFavoritesPostsByStudentId(1L)).thenReturn(Collections.emptyList());
        Mockito.when(favoriteService.getMyFavOthersFavoByStudentId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/favourite/getFavourite")
                .param("studentNum", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postList").exists())
                .andExpect(jsonPath("$.othersFavos").exists());
    }
} 