package com.example.edubjtu.controller;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.dto.PostComment;
import com.example.edubjtu.dto.ResourceList;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.model.Resource;
import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private TeacherService teacherService;

    @Test
    public void testGetCourses() throws Exception {
        when(courseService.getCoursesByTeacherId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/courses")
                .param("teacherId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    public void testGetCourseDetails() throws Exception {
        Course course = new Course();
        when(courseService.getCourseByCourseId(1L)).thenReturn(course);

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"));
    }

    @Test
    public void testGetCourseNotification() throws Exception {
        when(notificationService.getNotificationsByCourseId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/course/notification")
                .param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notifications").exists());
    }



} 