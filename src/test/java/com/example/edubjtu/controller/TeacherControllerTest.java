package com.example.edubjtu.controller;

import com.example.edubjtu.model.Comment;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private HomeWorkService homeworkService;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private HttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowDashboard_LoggedIn() throws Exception {
        Teacher teacher = new Teacher();
        when(session.getAttribute("loggedInTeacher")).thenReturn(teacher);

        mockMvc.perform(get("/teacher/dashboard").sessionAttr("loggedInTeacher", teacher))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacher").exists());
    }

    @Test
    public void testShowDashboard_NotLoggedIn() throws Exception {
        when(session.getAttribute("loggedInTeacher")).thenReturn(null);

        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }

    @Test
    public void testUploadPost_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(session.getAttribute("loggedInTeacher")).thenReturn(teacher);

        mockMvc.perform(post("/teacher/post")
                .param("courseId", "1")
                .param("title", "Test Title")
                .param("content", "Test Content")
                .sessionAttr("loggedInTeacher", teacher))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("帖子上传成功"));
    }

    @Test
    public void testUploadPost_NotLoggedIn() throws Exception {
        when(session.getAttribute("loggedInTeacher")).thenReturn(null);

        mockMvc.perform(post("/teacher/post")
                .param("courseId", "1")
                .param("title", "Test Title")
                .param("content", "Test Content"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("未登录，请重新登录"));
    }

    @Test
    public void testDeletePost_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Post post = new Post();
        post.setCourseId(1L);
        post.setTeacherId(1L);

        when(session.getAttribute("loggedInTeacher")).thenReturn(teacher);
        when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(delete("/teacher/course/1/post/1").sessionAttr("loggedInTeacher", teacher))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("帖子删除成功"));
    }

    @Test
    public void testDeletePost_NotAuthorized() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Post post = new Post();
        post.setCourseId(1L);
        post.setTeacherId(2L); // Different teacher ID

        when(session.getAttribute("loggedInTeacher")).thenReturn(teacher);
        when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(delete("/teacher/course/1/post/1").sessionAttr("loggedInTeacher", teacher))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("无权删除此帖子或帖子不存在"));
    }

    @Test
    public void testDeleteComment_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Comment comment = new Comment();
        comment.setCourseId(1L);
        comment.setTeacherId(1L);

        when(session.getAttribute("loggedInTeacher")).thenReturn(teacher);
        when(commentService.getCommentById(1L)).thenReturn(comment);

        mockMvc.perform(delete("/teacher/course/1/comment/1").sessionAttr("loggedInTeacher", teacher))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("评论删除成功"));
    }

    @Test
    public void testDeleteComment_NotAuthorized() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Comment comment = new Comment();
        comment.setCourseId(1L);
        comment.setTeacherId(2L); // Different teacher ID

        when(session.getAttribute("loggedInTeacher")).thenReturn(teacher);
        when(commentService.getCommentById(1L)).thenReturn(comment);

        mockMvc.perform(delete("/teacher/course/1/comment/1").sessionAttr("loggedInTeacher", teacher))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("无权删除此评论或评论不存在"));
    }
}
