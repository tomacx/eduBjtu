package com.example.edubjtu.controller;

import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.service.*;
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

    @Test
    public void testGetCourses() throws Exception {
        Mockito.when(courseService.getCoursesByTeacherId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/courses")
                .param("teacherId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    public void testGetCourseDetails() throws Exception {
        Course course = new Course();
        Mockito.when(courseService.getCourseByCourseId(1L)).thenReturn(course);

        mockMvc.perform(MockMvcRequestBuilders.get("/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"));
    }

    @Test
    public void testGetCourseNotification() throws Exception {
        Mockito.when(notificationService.getNotificationsByCourseId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/course/notification")
                .param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notifications").exists());
    }
} 