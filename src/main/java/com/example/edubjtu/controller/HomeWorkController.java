package com.example.edubjtu.controller;

import org.springframework.ui.Model;
import com.example.edubjtu.model.Homework;
import com.example.edubjtu.service.HomeWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeWorkController {
    @Autowired
    private HomeWorkService homeWorkService;

    @GetMapping("/homeworks")
    public String homework(Model model, @RequestParam Long courseId) {
        List<Homework> homeworkList=homeWorkService.getHomeworkByCourseId(courseId);
        model.addAttribute("homeworkList",homeworkList);
        return "homeworks";
    }
}
