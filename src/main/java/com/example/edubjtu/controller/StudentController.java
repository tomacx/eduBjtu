package com.example.edubjtu.controller;

import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student != null) {
            model.addAttribute("student", student);
            return "studentWelcome";  // 确保这个视图名称对应于正确的模板文件
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
}
