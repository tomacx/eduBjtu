package com.example.edubjtu.controller;

import com.example.edubjtu.dto.LoginDTO;
import com.example.edubjtu.model.Student;
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
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String studentNum, 
                        @RequestParam String password, 
                        HttpSession session) {
        Student student = studentService.findStudentByStudentNum(studentNum);
        if (student != null && student.getPassword().equals(password)) {
            session.setAttribute("loggedInStudent", student);
            logger.info("Student logged in: {}", student.getName());
            return "redirect:/student/dashboard";
        } else {
            logger.warn("Login failed for student number: {}", studentNum);
            return "redirect:/login?error";
        }
    }

    @GetMapping("/student/welcome")
    public String studentWelcome() {

        return "studentWelcome";
    }

    @GetMapping("/teacher/welcome")
    public String teacherWelcome() {
        return "teacherWelcome";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
