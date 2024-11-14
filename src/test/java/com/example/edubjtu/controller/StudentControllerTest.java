package com.example.edubjtu.controller;

import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private HomeWorkService homeworkService;

    @MockBean
    private PostService postService;

    @MockBean
    private HttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowDashboard_LoggedIn() throws Exception {
        Student student = new Student();
        when(session.getAttribute("loggedInStudent")).thenReturn(student);
        when(notificationService.getAllNotification()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/student/dashboard").sessionAttr("loggedInStudent", student))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student").exists())
                .andExpect(jsonPath("$.notifications").exists());
    }

    @Test
    public void testShowDashboard_NotLoggedIn() throws Exception {
        when(session.getAttribute("loggedInStudent")).thenReturn(null);

        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }

    @Test
    public void testUpdateStudent_Success() throws Exception {
        doNothing().when(studentService).updateStudentPassword(anyString(), anyString());

        mockMvc.perform(post("/student/update")
                .param("studentNum", "12345")
                .param("password", "newpassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Update successful"))
                .andExpect(jsonPath("$.studentNum").value("12345"));
    }

    @Test
    public void testUpdateStudent_Failure() throws Exception {
        doThrow(new RuntimeException()).when(studentService).updateStudentPassword(anyString(), anyString());

        mockMvc.perform(post("/student/update")
                .param("studentNum", "12345")
                .param("password", "newpassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Update failed"));
    }

    @Test
    public void testShowCourseDetail_CourseExists() throws Exception {
        Course course = new Course();
        when(courseService.getCourseByCourseId(1L)).thenReturn(course);

        mockMvc.perform(get("/student/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course").exists());
    }

    @Test
    public void testShowCourseDetail_CourseNotFound() throws Exception {
        when(courseService.getCourseByCourseId(1L)).thenReturn(null);

        mockMvc.perform(get("/student/course/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Course not found"));
    }

    @Test
    public void testGetStudentCourses() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(studentService.findStudentByStudentNum("12345")).thenReturn(student);
        when(courseService.getCourseByStudentId(1L)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/student/courses")
                .param("studentNum", "12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").exists());
    }

    @Test
    public void testDownloadResource_Success() throws Exception {
        when(resourceService.getResourceFilePath(1L, 1L)).thenReturn(Path.of("path/to/resource"));

        mockMvc.perform(get("/student/course/1/downloadResource/1"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''resource"));
    }

    @Test
    public void testDownloadResource_NotFound() throws Exception {
        when(resourceService.getResourceFilePath(1L, 1L)).thenThrow(new IOException("Could not read the file!"));

        mockMvc.perform(get("/student/course/1/downloadResource/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUploadStudentHomework_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "homework.pdf", "application/pdf", "content".getBytes());

        mockMvc.perform(multipart("/student/homework/1/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("作业上传成功"));
    }

    @Test
    public void testUploadStudentHomework_InvalidFileType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "homework.txt", "text/plain", "content".getBytes());

        mockMvc.perform(multipart("/student/homework/1/upload")
                .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("不支持的文件类型"));
    }

    @Test
    public void testSendPost_Success() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(session.getAttribute("loggedInStudent")).thenReturn(student);

        mockMvc.perform(post("/student/post")
                .param("courseId", "1")
                .param("title", "Test Title")
                .param("content", "Test Content")
                .sessionAttr("loggedInStudent", student))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("帖子发送成功"));
    }

    @Test
    public void testSendPost_NotLoggedIn() throws Exception {
        when(session.getAttribute("loggedInStudent")).thenReturn(null);

        mockMvc.perform(post("/student/post")
                .param("courseId", "1")
                .param("title", "Test Title")
                .param("content", "Test Content"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }
} 