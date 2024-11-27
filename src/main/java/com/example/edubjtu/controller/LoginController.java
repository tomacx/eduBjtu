package com.example.edubjtu.controller;

import com.example.edubjtu.model.FavoriteInfo;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.service.FavoriteInfoService;
import com.example.edubjtu.service.StudentService;
import com.example.edubjtu.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private FavoriteInfoService favoriteInfoService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String userNum,
                                                     @RequestParam String password,
                                                     HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Student student = studentService.findStudentByStudentNum(userNum);
        if (student != null && student.getPassword().equals(password)) {
            session.setAttribute("loggedInStudent", student);
            logger.info("Student logged in: {}", student.getName());
            response.put("success", true);
            response.put("redirect", "/student/dashboard"); // 重定向URL
            response.put("userName",student.getName());
            response.put("userNum",student.getStudentNum());
            //判断学生是否有收藏夹，没有的话就创建一个默认收藏夹
            FavoriteInfo favoriteInfo = favoriteInfoService.findByCreaterId(student.getId());
            if(favoriteInfo == null){
                favoriteInfoService.CreateFavoriteInfo(student.getId());
            }
            return ResponseEntity.ok(response);
        }

        Teacher teacher = teacherService.findTeacherByTeacherNum(userNum);
        if (teacher != null && teacher.getPassword().equals(password)) {
            session.setAttribute("loggedInTeacher", teacher);
            logger.info("Teacher logged in: {}", teacher.getName());
            response.put("success", true);
            response.put("redirect", "/teacher/dashboard"); // 重定向URL
            response.put("userName",teacher.getName());
            response.put("userNum",teacher.getTeacherNum());
            return ResponseEntity.ok(response);
        }

        logger.warn("Login failed for user number: {}", userNum);
        response.put("success", false);
        response.put("message", "登录失败"); // Failure message
        return ResponseEntity.badRequest().body(response); // 返回错误响应
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        return "redirect:/login?logout";
    }
}
