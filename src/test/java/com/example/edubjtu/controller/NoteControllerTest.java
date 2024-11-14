package com.example.edubjtu.controller;

import com.example.edubjtu.model.Note;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.NoteService;
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
@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private StudentService studentService;

    @Test
    public void testGetNote() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(studentService.findStudentByStudentNum("123")).thenReturn(student);
        Mockito.when(noteService.getNotesByStudentId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/note/getNote")
                .param("studentNum", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").exists());
    }

    @Test
    public void testViewNote() throws Exception {
        Note note = new Note();
        Mockito.when(noteService.getNoteById(1L)).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders.get("/note/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("note"));
    }
} 