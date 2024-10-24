package com.example.edubjtu.controller;

import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.service.StudentService;
import com.example.edubjtu.service.CourseService;
import com.example.edubjtu.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student != null) {
            model.addAttribute("student", student);
            List<Course> courses = courseService.getAllCourses(); // 获取所有课程信息
            model.addAttribute("courses", courses);

            // 获取通知信息
            List<Notification> notifications = notificationService.getAllNotification();
            model.addAttribute("notifications", notifications);

            return "studentWelcome"; // 返回学生欢迎页面
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student != null) {
            model.addAttribute("student", student);
            return "student-edit";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/update")
    public String updateStudent(@RequestParam("studentNum") String studentNum,
                                @RequestParam("password") String password) {
        studentService.updateStudentPassword(studentNum, password);
        return "redirect:/student/dashboard?studentNum=" + studentNum + "&success";
    }

    @GetMapping("/test")
    @ResponseBody
    public String testEndpoint() {
        return "Student controller is working";
    }

    @GetMapping("/course/{courseId}")
    public String showCourseDetail(@PathVariable Long courseId, Model model) {
        Course course = courseService.getCourseByCourseId(courseId);
        System.out.println(course);
        if (course == null) {
            return "error/404"; // 确保有404页面
        }
        model.addAttribute("course", course);
        return "courseDetails"; // 确保有courseDetails.html
    }
}
