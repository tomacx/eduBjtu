package com.example.edubjtu.controller;

import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.repository.CourseRepository;
import com.example.edubjtu.repository.StudentRepository;
import com.example.edubjtu.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


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

    @Autowired
    private HomeWorkService homeworkService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/dashboard")
    @ResponseBody // 添加此注解以返回 JSON
    public ResponseEntity<Map<String, Object>> showDashboard(HttpSession session) {
        Map<String, Object> modelMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");

        if (student != null) {
            modelMap.put("student", student);
            // 获取通知信息
            List<Notification> notifications = notificationService.getAllNotification();
            modelMap.put("notifications", notifications);

            return ResponseEntity.ok(modelMap); // 返回学生欢迎页面及数据
        } else {
            modelMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(modelMap); // 返回未授权状态
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

    @GetMapping("/courses")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentCourses(Model model, @RequestParam String studentNum){
        Map<String, Object> modelMap = new HashMap<>();
        System.out.println(studentNum);
        Student student = studentService.findStudentByStudentNum(studentNum);
        System.out.println(student.getId());
        List<Course> courses = courseRepository.findCoursesByStudentId(student.getId());
        modelMap.put("courses", courses);
        return ResponseEntity.ok(modelMap);
    }

    //TODO:增加学生端下载课程资源的功能
    @GetMapping("/course/{courseId}/downloadResource/{resourceId}")
    public ResponseEntity<Resource> downloadResource(@PathVariable Long courseId, @PathVariable Long resourceId) {
        try {
            Path filePath = resourceService.getResourceFilePath(courseId, resourceId);
            Resource fileResource = new UrlResource(filePath.toUri());

            if (fileResource.exists() || fileResource.isReadable()) {
                String encodedFileName = URLEncoder.encode(fileResource.getFilename(), StandardCharsets.UTF_8.toString());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''"+encodedFileName)
                        .body(fileResource);
            } else {
                throw new IOException("Could not read the file!");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //学生获取所有作业--done
    @GetMapping("/course/homework")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHomework(@RequestParam Long courseId, @RequestParam String studentNum) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("homeworkList",homeworkService.getHomeworkByCourseIdAndStudentNum(courseId,studentNum));
        return ResponseEntity.ok(modelMap);
    };

    //TODO:增加学生端上传作业的功能--done
    @PostMapping("course/homework/upload")
    public ResponseEntity<Map<String, Object>> uploadStudentHomework(@RequestParam Long homeworkId,
                                                                     @RequestParam String studentContent,
                                                                     @RequestParam("files") MultipartFile[] files) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        // 检查文件类型
        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (contentType != null && !isValidDocumentType(contentType)) {
                responseMap.put("error", "不支持的文件类型");
                return ResponseEntity.badRequest().body(responseMap);
            }
        }

        // 处理多个文件上传
        homeworkService.saveStudentHomework(homeworkId, studentContent, files);

        responseMap.put("message", "作业上传成功");
        return ResponseEntity.ok(responseMap);
    }


    // 验证文件类型
    private boolean isValidDocumentType(String contentType) {
        return contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.equals("application/vnd.ms-excel") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    //TODO:增加学生端搜索帖子的功能的功能

    //TODO:增加学生端对帖子评论、点赞、收藏的功能

    //TODO:增加学生端删除自己发送的帖子的功能
}
