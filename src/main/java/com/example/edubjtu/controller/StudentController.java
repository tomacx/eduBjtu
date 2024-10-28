package com.example.edubjtu.controller;

import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.model.Resource;
import com.example.edubjtu.repository.CourseRepository;
import com.example.edubjtu.service.StudentService;
import com.example.edubjtu.service.CourseService;
import com.example.edubjtu.service.NotificationService;
import com.example.edubjtu.service.ResourceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ResourceService resourceService;

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student != null) {
            model.addAttribute("student", student);
//            List<Course> courses = courseService.getAllCourses(); // 获取所有课程信息
            System.out.println(student.getStudentNum());
            List<Course> courses = courseRepository.findCoursesByStudentId(Long.valueOf(student.getId()));
            model.addAttribute("courses", courses);

            //用 Logger 记录课程信息
            logger.info("Courses: " + courses);

            System.out.println(courses);
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

//    @GetMapping("/courses")
//    public String getStudentCourses(Model model, @RequestParam Long studentId){
//        List<Course> courses = courseRepository.findCoursesByStudentId(studentId);
//        model.addAttribute("courses",courses);
//        return "studentCourses";
//    }

    @GetMapping("/course/{courseId}/resource/{resourceId}/download")
    public ResponseEntity<FileSystemResource> downloadResource(@PathVariable Long courseId, @PathVariable Long resourceId) {
        Resource resource = (Resource) resourceService.getResourcesByCourseId(courseId);
        File file = new File(resource.getFilePath());
        FileSystemResource fileResource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(fileResource);
    }

    //TODO:增加学生端下载课程资源的功能

    //TODO:增加学生端上传作业的功能

    //TODO:增加学生端搜索帖子的功能的功能

    //TODO:增加学生端对帖子评论、点赞、收藏的功能

    //TODO:增加学生端删除自己发送的帖子的功能
}
