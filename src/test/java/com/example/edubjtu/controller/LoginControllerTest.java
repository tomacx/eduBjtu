package com.example.edubjtu.controller;

import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Teacher;
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

import jakarta.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private HttpSession session;

    @Test
    public void testLogin_StudentSuccess() throws Exception {
        Student student = new Student();
        student.setStudentNum("123");
        student.setPassword("password");
        Mockito.when(studentService.findStudentByStudentNum("123")).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("userNum", "123")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirect").value("/student/dashboard"));
    }

    @Test
    public void testLogin_TeacherSuccess() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setTeacherNum("456");
        teacher.setPassword("password");
        Mockito.when(teacherService.findTeacherByTeacherNum("456")).thenReturn(teacher);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("userNum", "456")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirect").value("/teacher/dashboard"));
    }

    @Test
    public void testLogin_Failure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("userNum", "789")
                .param("password", "wrongpassword"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("登录失败"));
    }
} 