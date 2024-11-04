package com.example.edubjtu.controller;

import com.example.edubjtu.model.Resources;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private StudentService studentService;

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @GetMapping("/dashboard")
    @ResponseBody // 添加此注解以返回 JSON
    public ResponseEntity<Map<String, Object>> showDashboard(HttpSession session, Model model) {
        Map<String, Object> modelMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            modelMap.put("teacher", teacher);
            //TODO:把这里改成搜索只有该老师教的课程
//            List<Course> courses = courseService.getAllCourses(); // 获取所有课程信息
            return ResponseEntity.ok(modelMap);
        } else {
            modelMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(modelMap); // 返回未授权状态
        }
    }

    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            model.addAttribute("teacher", teacher);
            return "teacherEdit";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/update")
    public String updateTeacher(@RequestParam("teacherId") Long teacherId,
                                @RequestParam("password") String password) {
        teacherService.updateTeacherPassword(teacherId, password);
        return "redirect:/teacher/dashboard?success";
    }

    @GetMapping("/welcome")
    public String showTeacherCourses(@RequestParam Long teacherId, Model model) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        model.addAttribute("courses", courses);
        return "teacherWelcome"; // 确保有teacherWelcome.html
    }

    @GetMapping("/course/{courseId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showCourseDetail(@PathVariable Long courseId) {
        Map<String, Object> responseMap = new HashMap<>();
        Course course = courseService.getCourseByCourseId(courseId);
        if (course != null) {
            responseMap.put("course", course);
            List<Resources> resources = resourceService.getResourcesByCourseId(courseId);
            responseMap.put("resources", resources);
            return ResponseEntity.ok(responseMap);
        } else {
            responseMap.put("error", "课程未找到");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
    }

    //TODO：具体需要修改的课程信息的内容
    @PostMapping("/course/update")
    public String updateCourse(@ModelAttribute Course course) {
        courseService.updateCourse(course);
        return "redirect:/teacher/welcome";
    }

    @PostMapping("/course/{courseId}/upload")
    public String uploadResource(@PathVariable Long courseId, @RequestParam("file") MultipartFile file) {
        try {
            resourceService.saveResource(courseId, file);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
        }
        return "redirect:/teacher/course/" + courseId;
    }
    //TODO:增加老师上传通知的功能
    @PostMapping("/sendnotification")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestParam("title") String title,
                                                        @RequestParam("content") String content,
                                                        HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            // 假设有一个通知服务来处理通知的保存
            boolean success = notificationService.saveNotification(teacher.getId(), title, content);
            if (success) {
                responseMap.put("message", "通知上传成功");
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", "通知上传失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        } else {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
    //TODO:增加老师上传课程资源功能
    //TODO:还存在一些问题，文件的传输需要改一下
    @PostMapping("/course/{courseId}/uploadResourse")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> uploadResourse(@PathVariable Long courseId,
                                                             @RequestParam("file") MultipartFile file,
                                                             HttpSession session){
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if(teacher != null){
            try{
                resourceService.saveResource(courseId, file);
                responseMap.put("message","资源上传成功");
                return ResponseEntity.ok(responseMap);
            }catch(IOException e){
                logger.error("资源上传失败",e);
                responseMap.put("error","资源上传失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        }else{
            responseMap.put("error","未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
    //TODO:增加老师上传作业的功能
    
    //TODO:增加老师查看选课学生的功能
    @GetMapping("/course/{courseId}/students")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> viewStudents(@PathVariable Long courseId){
        Map<String, Object> responseMap = new HashMap<>();
        List<Student> students = studentService.findStudentsByCourseId(courseId);
        if (students != null){
            responseMap.put("students", students);
            return ResponseEntity.ok(responseMap);
        }else{
            responseMap.put("error", "未找到学生");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
    //TODO:增加老师查看帖子的功能

    //TODO:增加老师管理评论的功能

    //TODO:增加老师批阅作业的功能

    //TODO:增加老师删除帖子的功能

    //TODO:增加教师端查看成绩统计的功能
    }
}
