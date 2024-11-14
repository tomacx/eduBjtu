package com.example.edubjtu.controller;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.*;
import com.example.edubjtu.service.*;
import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

import java.io.IOException;
import java.util.Date;
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

    @Autowired
    private HomeWorkService homeworkService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;
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
            List<Resource> resources = resourceService.getResourcesByCourseId(courseId);
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
    //TODO：上传修改的课程信息
    @PostMapping("/course/{courseId}/upload")
    public String uploadResource(@PathVariable Long courseId, @RequestParam("file") MultipartFile file) {
        try {
            resourceService.saveCourseResource(courseId, file);
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
    @PostMapping("/course/{courseId}/uploadResourse")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> uploadResourse(@PathVariable Long courseId,
                                                             @RequestParam("file") MultipartFile file,
                                                             HttpSession session){
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if(teacher != null){
            try{
                resourceService.saveCourseResource(courseId, file);
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
    @PostMapping("/course/{courseId}/uploadHomework")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadHomework(@PathVariable Long courseId,
                                                              @RequestParam("file") MultipartFile file,
                                                              @RequestParam("homeworkNum") Integer homeworkNum,
                                                              @RequestParam("deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
                                                              @RequestParam("content") String content,
                                                              HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            try {
                System.out.println(deadline);
                homeworkService.saveHomework(courseId, homeworkNum, deadline, content, file);
                responseMap.put("message", "作业上传成功");
                return ResponseEntity.ok(responseMap);
            } catch (IOException e) {
                logger.error("作业上传失败", e);
                responseMap.put("error", "作业上传失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        } else {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
    //TODO:增加老师查看选课学生的功能
    @GetMapping("/course/{courseId}/students")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> viewStudents(@PathVariable Long courseId) {
        Map<String, Object> responseMap = new HashMap<>();
        List<Student> students = studentService.findStudentsByCourseId(courseId);
        if (students != null) {
            responseMap.put("students", students);
            return ResponseEntity.ok(responseMap);
        } else {
            responseMap.put("error", "未找到学生");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
    }
    //TODO:老师上传帖子
    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadPost(@RequestParam("courseId") Long courseId,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("content") String content,
                                                          HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            try {
                // 创建一个新的Post对象
                Post post = new Post();
                post.setCourseId(courseId);
                post.setTeacherId(teacher.getId());
                post.setTitle(title);
                post.setContent(content);

                // 使用PostService保存帖子
                postService.savePost(post);

                responseMap.put("message", "帖子上传成功");
                responseMap.put("postId", post.getPostId());
                return ResponseEntity.ok(responseMap);
            } catch (Exception e) {
                logger.error("帖子上传失败", e);
                responseMap.put("error", "帖子上传失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        } else {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
    //TODO:增加老师查看帖子的功能
    @GetMapping("/course/{courseId}/posts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> viewPosts(@PathVariable Long courseId, HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            try {
                // 获取指定课程的所有帖子
                List<CoursePost> posts = postService.getPostsByCourseId(courseId);
                responseMap.put("posts", posts);
                return ResponseEntity.ok(responseMap);
            } catch (Exception e) {
                logger.error("获取帖子失败", e);
                responseMap.put("error", "获取帖子失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        } else {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
    //TODO:增加老师管理评论的功能
    @DeleteMapping("/course/{courseId}/comment/{commentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long courseId,
                                                             @PathVariable Long commentId,
                                                             HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            try {
                // 检查评论是否属于该教师的课程
                Comment comment = commentService.getCommentById(commentId);
                if (comment != null && comment.getCourseId().equals(courseId) && comment.getTeacherId().equals(teacher.getId())) {
                    commentService.deleteComment(commentId);
                    responseMap.put("message", "评论删除成功");
                    return ResponseEntity.ok(responseMap);
                } else {
                    responseMap.put("error", "无权删除此评论或评论不存在");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap);
                }
            } catch (Exception e) {
                logger.error("评论删除失败", e);
                responseMap.put("error", "评论删除失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        } else {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
    //TODO:增加老师批阅作业的功能（包括下载作业资源）
    @PostMapping("/gradeHomework")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> gradeHomework(@RequestParam("homeworkNum") Integer homeworkNum,
                @RequestParam("studentNum") String studentNum,
                @RequestParam("score") Integer score){
            Map<String, Object> responseMap = new HashMap<>();
            try {
                homeworkService.gradeStudentHomework(homeworkNum, studentNum, score);
                responseMap.put("message", "作业批阅成功");
                return ResponseEntity.ok(responseMap);
            } catch (IllegalArgumentException e) {
                responseMap.put("error", e.getMessage());
                return ResponseEntity.badRequest().body(responseMap);
            } catch (IOException e) {
                responseMap.put("error", "批阅作业失败: " + e.getMessage());
                return ResponseEntity.status(500).body(responseMap);
            }
        }
    //TODO:增加老师删除帖子的功能
    @DeleteMapping("/course/{courseId}/post/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long courseId,
                                                          @PathVariable Long postId,
                                                          HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher != null) {
            try {
                // 检查帖子是否属于该教师的课程
                Post post = postService.getPostById(postId);
                if (post != null && post.getCourseId().equals(courseId) && post.getTeacherId().equals(teacher.getId())) {
                    postService.deletePost(postId);
                    responseMap.put("message", "帖子删除成功");
                    return ResponseEntity.ok(responseMap);
                } else {
                    responseMap.put("error", "无权删除此帖子或帖子不存在");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap);
                }
            } catch (Exception e) {
                logger.error("帖子删除失败", e);
                responseMap.put("error", "帖子删除失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
        } else {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
    }
    //TODO:增加教师端查看成绩统计的功能

   
    }

