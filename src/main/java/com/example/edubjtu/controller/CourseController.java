package com.example.edubjtu.controller;

import com.example.edubjtu.model.Course;
import com.example.edubjtu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public String getCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "studentWelcome";
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
}
