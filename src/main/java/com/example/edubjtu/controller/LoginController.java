package com.example.edubjtu.controller;

import com.example.edubjtu.dto.LoginDTO;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.service.StudentService;
import com.example.edubjtu.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login"; // 显示登录页面
    }

    @PostMapping("/login")
    public String login(@RequestParam String userNum,
                        @RequestParam String password,
                        HttpSession session) {
        Student student = studentService.findStudentByStudentNum(userNum);
        if (student != null && student.getPassword().equals(password)) {
            session.setAttribute("loggedInStudent", student);
            logger.info("Student logged in: {}", student.getName());
            return "redirect:/student/dashboard"; // 重定向到学生仪表板
        }

        Teacher teacher = teacherService.findTeacherByTeacherNum(userNum);
        if (teacher != null && teacher.getPassword().equals(password)) {
            session.setAttribute("loggedInTeacher", teacher);
            logger.info("Teacher logged in: {}", teacher.getName());
            return "redirect:/teacher/dashboard"; // 重定向到教师仪表板
        }

        logger.warn("Login failed for user number: {}", userNum);
        return "redirect:/login?error"; // 登录失败时重定向
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 注销时使会话失效
        return "redirect:/login"; // 重定向到登录页面
    }
}
