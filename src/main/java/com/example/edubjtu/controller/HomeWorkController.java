package com.example.edubjtu.controller;

import com.example.edubjtu.model.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.edubjtu.model.Homework;
import com.example.edubjtu.service.HomeWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeWorkController {
    @Autowired
    private HomeWorkService homeWorkService;

    //  返回该课程的所有学生提交的所有作业实体
    @GetMapping("/homeworks")
    @ResponseBody  // 添加此注解以返回 JSON
    public Map<String, Object> homeworks(@RequestParam Long courseId) {
        List<Homework> homeworkList = homeWorkService.getHomeworkByCourseId(courseId);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("homeworkList", homeworkList);
        return modelMap;
    }


    //  提供作业号返回指定作业
    @GetMapping("/homework")
    @ResponseBody
    public Map<String, Object> homework(@RequestParam Long homeworkId) {
        Homework homework=homeWorkService.getHomeworkByHomeworkId(homeworkId);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("homework", homework);
        return modelMap;
    }


}
