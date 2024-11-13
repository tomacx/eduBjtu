package com.example.edubjtu.controller;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.repository.CourseRepository;
import com.example.edubjtu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private HomeWorkService homeworkService;

    @Autowired
    private PostService postService;
    @GetMapping("/courses")
    public String getCourses(Model model, @RequestParam Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        model.addAttribute("courses", courses);
        return "teacherCourses"; // 返回显示课程的视图
    }

    @GetMapping("/{courseId}")
    public String getCourseDetails(@PathVariable Long courseId, Model model) {
        Course course = courseService.getCourseByCourseId(courseId);
        if (course == null) {
            return "error/404"; // 确保有404页面
        }
        model.addAttribute("course", course);
        return "courseDetails"; // 确保有courseDetails.html
    }

    @GetMapping("/course/{courseId}")
    public String getCourseDetail(@PathVariable Long courseId, Model model){
        Course course = courseService.getCourseByCourseId(courseId);
        if(course == null){
            return "error/404";
        }
        model.addAttribute("course",course);
        return "courseDetail";
    }
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "courseEdit";
    }

    @PostMapping("/{id}/update")
    public String updateCourse(@PathVariable Long id,
                               @RequestParam("intro") String intro,
                               @RequestParam("outline") String outline,
                               @RequestParam("teacherInfo") String teacherInfo) {
        courseService.updateCourseInfo(id, intro, outline, teacherInfo);
        return "redirect:/course/" + id + "/edit?success";
    }

    @PostMapping("course/{id}/upload")
    public String uploadResource(@PathVariable Long id,
                                 @RequestParam("file") MultipartFile file) {
        try {
            courseService.saveResource(id, file);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/course/" + id + "/edit?error";
        }
        return "redirect:/course/" + id + "/edit?success";
    }

    //课程通知
    @GetMapping("/course/notification")
    @ResponseBody
    public  ResponseEntity<Map<String,Object>> getCourseNotification(@RequestParam Long courseId){
        Map<String,Object> modelMap = new HashMap<>();
        List<Notification> notifications=notificationService.getNotificationsByCourseId(courseId);
        modelMap.put("notifications",notifications);
        return ResponseEntity.ok(modelMap);
    }

    //课程讨论
    @GetMapping("/course/discussion")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDiscussion(@RequestParam Long courseId) {
        System.out.println("Received courseId: " + courseId); // 打印 courseId
        Map<String, Object> modelMap = new HashMap<>();
        List<CoursePost> posts = postService.getPostsByCourseId(courseId);
        modelMap.put("posts", posts);
        return ResponseEntity.ok(modelMap);
    }

}
