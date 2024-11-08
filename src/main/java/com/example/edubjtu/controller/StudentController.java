package com.example.edubjtu.controller;

import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Resources;
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

    @Autowired
    private PostService postService;

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
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showEditForm(HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student != null) {
            responseMap.put("student", student);
            return ResponseEntity.ok(responseMap);
        } else {
            responseMap.put("redirect", "/login");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateStudent(@RequestParam("studentNum") String studentNum,
                                                             @RequestParam("password") String password) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            studentService.updateStudentPassword(studentNum, password);
            responseMap.put("message", "Update successful");
            responseMap.put("studentNum", studentNum);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            responseMap.put("error", "Update failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }
    
    @GetMapping("/course/{courseId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showCourseDetail(@PathVariable Long courseId) {
        Map<String, Object> responseMap = new HashMap<>();
        Course course = courseService.getCourseByCourseId(courseId);
        System.out.println(course);
        if (course == null) {
            responseMap.put("error", "Course not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
        responseMap.put("course", course);
        return ResponseEntity.ok(responseMap);
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
    //TODO:增加学生端上传作业的功能
    @PostMapping("/homework/{homeworkId}/upload")
    public ResponseEntity<Map<String,Object>> uploadStudentHomework(@PathVariable Long homeworkId,
                                                                    @RequestParam("file")MultipartFile file){
        Map<String, Object> responseMap = new HashMap<>();
        try{
            //检查文件类型
            String contentType = file.getContentType();
            if (!isValidDocumentType(contentType)){
                responseMap.put("error","不支持的文件类型");
                return ResponseEntity.badRequest().body(responseMap);
            }
            homeworkService.saveStudentHomework(homeworkId, file);
            responseMap.put("message","作业上传成功");
            return ResponseEntity.ok(responseMap);
        } catch (IOException e) {
            responseMap.put("error","作业上传失败");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    // 验证文件类型
    private boolean isValidDocumentType(String contentType) {
        return contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.equals("application/vnd.ms-excel") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    //TODO:发送帖子
    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendPost(@RequestParam("courseId") Long courseId,
                                                        @RequestParam("title") String title,
                                                        @RequestParam("content") String content,
                                                        HttpSession session){
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student == null){
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
        
        Post post = new Post();
        post.setCourseId(courseId);
        post.setStudentId(student.getId());
        post.setTitle(title);
        post.setContent(content);
        postService.savePost(post);

        responseMap.put("message", "帖子发送成功");
        responseMap.put("postId", post.getPostId());
        return ResponseEntity.ok(responseMap);
    }
    //TODO:发送评论

    //TODO:增加学生端搜索帖子的功能的功能

    //TODO:增加学生端对帖子评论、点赞、收藏的功能

    //TODO:增加学生端删除自己发送的帖子的功能
}
