package com.example.edubjtu.controller;


import com.example.edubjtu.dto.LoginDto;
import com.example.edubjtu.entity.Student;
import com.example.edubjtu.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * (Student)表控制层
 *
 * @author zoupeng
 * @since 2024-10-17 20:02:15
 */
@RestController
@RequestMapping("student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDto dto)throws Exception {
        Student student = studentService.login(dto);
        Map<String, Object> map = new HashMap<>();
        map.put("status", 200);
        map.put("user", student);
        return map;
    }
}

