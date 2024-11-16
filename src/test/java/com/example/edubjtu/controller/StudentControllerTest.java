package com.example.edubjtu.controller;

import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.repository.CourseRepository;
import com.example.edubjtu.repository.StudentRepository;
import com.example.edubjtu.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private CourseRepository courseRepository;

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

    @MockBean
    private CommentService commentService;
    //测试登录面板
    @Test
    public void testShowDashboard_LoggedIn() throws Exception {
        Student student = new Student();
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(student);
        Mockito.when(notificationService.getAllNotification()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/student/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student").exists())
                .andExpect(jsonPath("$.notifications").exists());
    }
    //测试登录面板
    @Test
    public void testShowDashboard_NotLoggedIn() throws Exception {
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/dashboard"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }
    //测试登陆后点击编辑按钮
    @Test
    public void testShowEditForm_LoggedIn() throws Exception {
        Student student = new Student();
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/edit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student").exists());
    }
    //测试没登陆点击编辑按钮
    @Test
    public void testShowEditForm_NotLoggedIn() throws Exception {
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/edit"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.redirect").value("/login"));
    }
    //测试学生修改自己的密码
    @Test
    public void testUpdateStudent_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/student/update")
                .param("studentNum", "123")
                .param("password", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Update successful"));
    }
    //测试学生点开一门具体的课 找到的
    @Test
    public void testShowCourseDetail_Found() throws Exception {
        Course course = new Course();
        Mockito.when(courseService.getCourseByCourseId(anyLong())).thenReturn(course);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course").exists());
    }
    //测试学生点开一门具体的课 没有找到
    @Test
    public void testShowCourseDetail_NotFound() throws Exception {
        Mockito.when(courseService.getCourseByCourseId(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/course/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Course not found"));
    }
    //测试学生能否收到课程列表
    @Test
    public void testGetStudentCourses() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(studentService.findStudentByStudentNum(anyString())).thenReturn(student);
        Mockito.when(courseRepository.findCoursesByStudentId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/student/courses")
                .param("studentNum", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").exists());
    }
    //测试能否得到通知
    @Test
    public void testGetNotification() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(studentService.findStudentByStudentNum(anyString())).thenReturn(student);
        Mockito.when(notificationService.getNotificationsByStudentNum(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/student/getNotification")
                .param("studentNum", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notifications").exists())
                .andExpect(jsonPath("$.notificationNum").value(0));
    }
    //测试能否得到作业
    @Test
    public void testGetHomework() throws Exception {
        Mockito.when(homeworkService.getHomeworkByCourseIdAndStudentNum(anyLong(), anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/student/course/homework")
                .param("courseId", "1")
                .param("studentNum", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeworkList").exists());
    }
    //测试上传作业
    @Test
    public void testUploadStudentHomework_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/student/course/homework/upload")
                .file("files", "test content".getBytes())
                .param("homeworkId", "1")
                .param("studentContent", "content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("作业上传成功"));
    }
    //测试发送帖子的两种情况
    @Test
    public void testSendPost_NotLoggedIn() throws Exception {
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/post")
                .param("courseId", "1")
                .param("title", "Test Title")
                .param("content", "Test Content"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }

    @Test
    public void testSendPost_LoggedIn() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/post")
                .param("courseId", "1")
                .param("title", "Test Title")
                .param("content", "Test Content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("帖子发送成功"));
    }

    //测试给帖子发送评论的两种情况
    @Test
    public void testSendComment_LoggedIn() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/post/1/comment")
                        .param("content", "Test Comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("评论发送成功"));
    }

    @Test
    public void testSendComment_NotLoggedIn() throws Exception {
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/post/1/comment")
                        .param("content", "Test Comment"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }
    //测试回复评论
    @Test
    public void testReplyToComment_LoggedIn() throws Exception {
        Student student = new Student();
        student.setId(1L);
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/comment/1/reply")
                        .param("content", "Test Reply"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("回复发送成功"));
    }

    @Test
    public void testReplyToComment_NotLoggedIn() throws Exception {
        Mockito.when(session.getAttribute("loggedInStudent")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/student/comment/1/reply")
                        .param("content", "Test Reply"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }
} 