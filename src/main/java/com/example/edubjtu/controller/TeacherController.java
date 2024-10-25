package com.example.edubjtu.controller;

import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.service.TeacherService;
import com.example.edubjtu.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            model.addAttribute("teacher", teacher);
            // List<Course> courses = courseService.getCoursesByTeacher(teacher); // 获取该教师教的课程信息
            // model.addAttribute("courses", courses);
            //TODO:把这里改成搜索只有该老师教的课程
            List<Course> courses = courseService.getAllCourses(); // 获取所有课程信息
            model.addAttribute("courses", courses);
            return "teacherWelcome"; // 返回教师欢迎页面
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            model.addAttribute("teacher", teacher);
            return "teacherEdit";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/update")
    public String updateTeacher(@RequestParam("teacherId") Long teacherId,
                                @RequestParam("password") String password) {
        teacherService.updateTeacherPassword(teacherId, password);
        return "redirect:/teacher/dashboard?success";
    }

    @GetMapping("/welcome")
    public String showTeacherCourses(@RequestParam Long teacherId, Model model) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        model.addAttribute("courses", courses);
        return "teacherWelcome"; // 确保有teacherWelcome.html
    }

    @GetMapping("/courseDetail/{courseId}")
    public String showCourseDetail(@PathVariable Long courseId, Model model) {
        // 处理逻辑
        return "courseDetail";
    }

    @PostMapping("/course/update")
    public String updateCourse(@ModelAttribute Course course) {
        courseService.updateCourse(course);
        return "redirect:/teacher/welcome";
    }
}
