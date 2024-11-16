package com.example.edubjtu.controller;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.*;
import com.example.edubjtu.service.*;
import jakarta.persistence.Entity;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
            List<Course> courses = courseService.getCoursesByTeacherId(teacher.getId());
//            List<Course> courses = courseService.getAllCourses(); // 获取所有课程信息
            //TODO:这块前端通过查询course,得到课程列表
            modelMap.put("courses",courses);
            return ResponseEntity.ok(modelMap);
        } else {
            modelMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(modelMap); // 返回未授权状态
        }
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

    //老师上传课程大纲
    @PostMapping("/course/{courseId}/uploadCourseOutLine")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadCourseOutLine(
            @PathVariable Long courseId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {

        Map<String, Object> responseMap = new HashMap<>();

        try {
            // 调用服务层保存OutLine
            courseService.saveOutLine(courseId, file);

            // 返回成功消息
            responseMap.put("message", "大纲上传成功");
            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            logger.error("上传过程中出现错误", e);
            responseMap.put("error", "大纲上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    //老师上传课程时间
    @PostMapping("/course/{courseId}/uploadCourseCalendar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadCourseCalendar(
            @PathVariable Long courseId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {

        Map<String, Object> responseMap = new HashMap<>();

        try {
            // 调用服务层保存OutLine
            courseService.saveCalendar(courseId, file);

            // 返回成功消息
            responseMap.put("message", "大纲上传成功");
            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            logger.error("上传过程中出现错误", e);
            responseMap.put("error", "大纲上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    //增加老师上传通知的功能--done
    @PostMapping("/sendNotification")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestParam String title,
                                                                @RequestParam String content,
                                                                @RequestParam String teacherNum,
                                                                @RequestParam Long courseId) {
        Map<String, Object> responseMap = new HashMap<>();
        Teacher teacher = teacherService.findTeacherByTeacherNum(teacherNum);
        if (teacher != null) {
            // 假设有一个通知服务来处理通知的保存
            boolean success = notificationService.saveNotification(teacher.getId(), title, content, courseId);

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

    //老师上传试题功能--done
    @PostMapping("/course/{courseId}/uploadWorkSet")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadWorkSet(@PathVariable Long courseId, @RequestParam("file") MultipartFile[] file) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        resourceService.saveCourseWorkSets(courseId, file);
        responseMap.put("message", "习题集上传成功");
        return ResponseEntity.ok(responseMap);
    }

    //homepage课程获取--done
    @GetMapping("/courses")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentCourses(@RequestParam String teacherNum) {
        Map<String, Object> modelMap = new HashMap<>();
        System.out.println(teacherNum);
        Teacher teacher = teacherService.findTeacherByTeacherNum(teacherNum);
        System.out.println(teacher.getId());
        List<Course> courses = courseService.getCoursesByTeacherId(teacher.getId());
        modelMap.put("courses", courses);
        return ResponseEntity.ok(modelMap);
    }

    //增加老师上传课程资源功能 课件等--done
    @PostMapping("/course/{courseId}/uploadResource")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadResource(@PathVariable Long courseId,
                                                              @RequestParam("file") MultipartFile[] file) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();

        resourceService.saveCourseResources(courseId, file);
        responseMap.put("message", "资源上传成功");
        return ResponseEntity.ok(responseMap);

    }
    //TODO 增加老师上传作业的功能 待连接前端
    @PostMapping("/course/{courseId}/uploadHomework")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadHomework(
            @PathVariable Long courseId,
            @RequestParam int homeworkNum,
            @RequestParam String deadline,  // deadline 为传入的字符串
            @RequestParam(required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {

        Map<String, Object> responseMap = new HashMap<>();

        try {
            // 解析 deadline 字符串为 Date 类型
            Date parsedDeadline = parseDeadline(deadline);
            if (parsedDeadline == null) {
                responseMap.put("error", "无效的截止时间格式");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
            }

            // 调用服务层保存作业信息
            homeworkService.saveHomework(courseId, homeworkNum, parsedDeadline, content, file);

            // 返回成功消息
            responseMap.put("message", "作业上传成功");
            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            logger.error("上传过程中出现错误", e);
            responseMap.put("error", "作业上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    //老师已发布的作业list
    @GetMapping("/course/{courseId}/homeworkList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHomeworkList(@PathVariable Long courseId) {
        Map<String, Object> responseMap = new HashMap<>();
        List<Homework> homeworkList = homeworkService.getFirstHomeworkByCourseId(courseId);
        responseMap.put("homeworkList", homeworkList);
        return ResponseEntity.ok(responseMap);
    }

    // 解析 deadline 字符串为 Date 类型，使用 ISO 8601 格式--done
    private Date parseDeadline(String deadline) {
        try {
            // 解析 ISO 8601 格式的时间字符串（例如 "2024-11-08T23:59:59.000Z"）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");  // X 处理时区偏移
            return sdf.parse(deadline);
        } catch (Exception e) {
            // 如果格式不正确，返回 null
            return null;
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
                if (comment != null && comment.getTeacherId().equals(teacher.getId())) {
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
                                                             @RequestParam("score") Integer score) {
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

