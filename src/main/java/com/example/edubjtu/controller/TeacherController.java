package com.example.edubjtu.controller;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.model.*;
import com.example.edubjtu.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public ResponseEntity<Map<String, Object>> showDashboard(HttpSession session) {
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
            courseService.saveOutLine(courseId,file);

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
            courseService.saveCalendar(courseId,file);

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
            boolean success = notificationService.saveNotification(teacher.getId(), title, content,courseId);

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
    public ResponseEntity<Map<String,Object>> uploadWorkSet(@PathVariable Long courseId, @RequestParam("file") MultipartFile[] file) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        resourceService.saveCourseWorkSets(courseId,file);
        responseMap.put("message","习题集上传成功");
        return ResponseEntity.ok(responseMap);
    }
    //homepage课程获取--done
    @GetMapping("/courses")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentCourses(@RequestParam String teacherNum){
        Map<String, Object> modelMap = new HashMap<>();
        System.out.println(teacherNum);
        Teacher teacher=teacherService.findTeacherByTeacherNum(teacherNum);
        System.out.println(teacher.getId());
        List<Course> courses = courseService.getCoursesByTeacherId(teacher.getId());
        modelMap.put("courses", courses);
        return ResponseEntity.ok(modelMap);
    }

    //增加老师上传课程资源功能 课件等--done
    @PostMapping("/course/{courseId}/uploadResource")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> uploadResource(@PathVariable Long courseId,
                                                             @RequestParam("file") MultipartFile[] file) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();

            resourceService.saveCourseResources(courseId, file);
            responseMap.put("message","资源上传成功");
            return ResponseEntity.ok(responseMap);

    }

    //老师根据资源id删除已经上传的资源内容,公用于课件内容和习题库
    @DeleteMapping("/course/deleteResource/{resourceId}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> deleteResource(@PathVariable Long resourceId){
        Map<String, Object> responseMap = new HashMap<>();
        if (resourceService.deleteCourseResourceByResourceId(resourceId)) {
            responseMap.put("message","成功删除资源");
        }else{
            responseMap.put("message","删除资源失败");
        }
        return ResponseEntity.ok(responseMap);

    }

    // 增加老师上传作业的功能 待连接前端
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
                homeworkService.saveHomework(courseId, homeworkNum, parsedDeadline, content,file);

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

        // 获取课程的作业列表
        List<Homework> homeworkList = homeworkService.getFirstHomeworkByCourseId(courseId);
        List<Map<String, Object>> homeworkStats = new ArrayList<>();

        // 遍历作业列表，为每个作业统计提交情况
        for (Homework homework : homeworkList) {
            List<Homework> list = homeworkService.getHomeworkByCourseIdAndHomeworkNum(homework.getHomeworkNum(), courseId);
            //统计平均分
            float totalGrade=0;
            // 统计已提交作业人数
            int alreadySubmit = 0;
            for (Homework homework1 : list) {
                if (homework1.getSubmitCheck() != null && homework1.getSubmitCheck() == 1) {
                    alreadySubmit++;
                }
                if(homework1.getGrade()!=null){
                    totalGrade+=homework1.getGrade();
                }
            }

            // 获取总作业人数
            int totalNum = list.size();
            //均分
            float avgGrade = totalGrade / alreadySubmit;

            // 创建一个新的Map来存储作业的提交情况
            Map<String, Object> stats = new HashMap<>();
            stats.put("homeworkNum", homework.getHomeworkNum());
            stats.put("alreadySubmit", alreadySubmit);
            stats.put("totalNum", totalNum);
            stats.put("avgGrade", avgGrade);
            // 将统计数据添加到列表中
            homeworkStats.add(stats);
        }

        // 将作业信息和提交统计数据放入responseMap
        responseMap.put("homeworkList", homeworkList);
        responseMap.put("homeworkStats", homeworkStats);

        return ResponseEntity.ok(responseMap);
    }


    //老师查看作业详情,返回改作业的所有学生列表
    @GetMapping("/course/homework/homeworkDetailList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentHomework(@RequestParam int homeworkNum, @RequestParam long courseId) {
        List<Homework> homeworkList = homeworkService.getHomeworkByCourseIdAndHomeworkNum(homeworkNum, courseId);
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Object>> homeworkDetailsList = new ArrayList<>();

        // 遍历作业列表
        for (Homework homework : homeworkList) {
            // 创建一个新的 Map 用于存储每个作业的信息
            Map<String, Object> homeworkDetails = new HashMap<>();
            // 向 Map 中加入基本信息
            homeworkDetails.put("homeworkId", homework.getHomeworkId());
            homeworkDetails.put("homeworkNum", homework.getHomeworkNum());
            homeworkDetails.put("courseId", courseId);
            homeworkDetails.put("studentNum", homework.getStudentNum());
            homeworkDetails.put("studentName", studentService.getStudentByStudentNum(homework.getStudentNum()).getName());
            homeworkDetails.put("grade", homework.getGrade());
            homeworkDetails.put("mutualGrade", homework.getMutualGrade());
            homeworkDetails.put("submitCheck", homework.getSubmitCheck());

            if (homework.getGrade() != null) {
                homeworkDetails.put("correctCheck", 1);
            } else {
                homeworkDetails.put("correctCheck", 0);
            }

            // 将每个作业的详细信息添加到列表中
            homeworkDetailsList.add(homeworkDetails);
        }

        // 将所有作业详情的列表放入 responseMap 中
        responseMap.put("homeworkDetailsList", homeworkDetailsList);

        return ResponseEntity.ok(responseMap);
    }


    //老师批改作业获取改作业内容
    @GetMapping("/course/homework/homeworkCorrect")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentHomeworkCorrect(@RequestParam Long homeworkId) {
        Homework homework = homeworkService.getHomeworkByHomeworkId(homeworkId);
        Resource resource = resourceService.getResourceByHomeworkId(homeworkId);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("homework", homework);
        if (resource!=null) {
            String filePath = resource.getFilePath();
            //src\main\resources\static\course\Session_0_Requirements.pptx
            // 替换路径中的 `src\main` 为 `static`，并将反斜杠替换为正斜杠
            String adjustedFilePath = filePath.replace("src\\main\\resources\\static\\", "")
                    .replace("\\", "/");

            // 构造完整的 URL
            String fileUrl = "http://localhost:8000/" + adjustedFilePath;
            modelMap.put("URL", fileUrl);
            String fileType = resource.getFileType(); // 获取文件类型信息
            modelMap.put("fileType", fileType);
        } else {
            modelMap.put("URL", "Not Found");
        }
        return ResponseEntity.ok(modelMap);
    };

    //老师提交作业分数
    @PostMapping("course/homework/submitGrade")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitGrade(@RequestParam Long homeworkId,@RequestParam int grade){
        Homework homework = homeworkService.getHomeworkByHomeworkId(homeworkId);
        homework.setGrade(grade);
        homeworkService.save(homework);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "分数提交成功");
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

    //老师端成绩统计
    @GetMapping("/course/homework/static")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentHomeworkStatic( @RequestParam long courseId,@RequestParam int homeworkNum) {
        List<Homework> homeworkList = homeworkService.getHomeworkByCourseIdAndHomeworkNum(homeworkNum, courseId);
        int grade_100_90=0;
        int grade_90_80=0;
        int grade_80_70=0;
        int grade_70_60=0;
        int grade_60_0=0;
        for (Homework homework : homeworkList){
            if(homework.getGrade()!=null){
                if(homework.getGrade()>=90 && homework.getGrade()<=100 ) grade_100_90++;
                else if(homework.getGrade()>=80 && homework.getGrade()<90 ) grade_90_80++;
                else if(homework.getGrade()>=70 && homework.getGrade()<80 ) grade_80_70++;
                else if(homework.getGrade()>=60 && homework.getGrade()<70 ) grade_70_60++;
                else grade_60_0++;
            }
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("grade_100_90", grade_100_90);
        responseMap.put("grade_90_80", grade_90_80);
        responseMap.put("grade_80_70", grade_80_70);
        responseMap.put("grade_70_60", grade_70_60);
        responseMap.put("grade_60_0", grade_60_0);
        return  ResponseEntity.ok(responseMap);
    }
    //发送评论--done
    @PostMapping("/post/setComment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendComment(@RequestParam Long postId,
                                                           @RequestParam String teacherNum,
                                                           @RequestParam("content") String content
    ) {
        Map<String, Object> responseMap = new HashMap<>();
        String deContent =  URLDecoder.decode(content, StandardCharsets.UTF_8);
        Post post = postService.getPostById(postId);
        //由后端判断回复的帖子是老师的还是学生的
        //评论的老师
        Teacher teacher = teacherService.findTeacherByTeacherNum(teacherNum);
        if (post.getStudentId()!=null) {
            Optional<Student> commentedStudent = studentService.findStudentById(post.getStudentId());
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setLikeNum(0);
            comment.setCommentedNum(commentedStudent.get().getStudentNum());
            comment.setContent(deContent);
            comment.setTeacherId(teacher.getId());
            commentService.saveComment(comment);
            responseMap.put("message", "评论发送成功");
            responseMap.put("commentId", comment.getCommentId());
            return ResponseEntity.ok(responseMap);
        }else{
            //回复的帖子是老师
            Optional<Teacher> commentedTeacher = teacherService.findTeacherById(post.getTeacherId());
            if (commentedTeacher.isPresent()) {
                Comment comment = new Comment();
                comment.setPostId(postId);
                comment.setLikeNum(0);
                comment.setCommentedNum(commentedTeacher.get().getTeacherNum());
                comment.setContent(deContent);
                comment.setTeacherId(teacher.getId());
                commentService.saveComment(comment);
                responseMap.put("message", "评论发送成功");
                responseMap.put("commentId", comment.getCommentId());
                return ResponseEntity.ok(responseMap);
            }
        }
        responseMap.put("message", "评论发送失败");
        responseMap.put("commentId", "");
        return ResponseEntity.ok(responseMap);
    }

    //发送帖子--done
    @PostMapping("/setPost")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendPost(@RequestParam("courseId") Long courseId,
                                                        @RequestParam("title") String title,
                                                        @RequestParam("content") String content,
                                                        @RequestParam("teacherNum") String teacherNum
    ){
        Map<String, Object> responseMap = new HashMap<>();
        // URL 解码，使用 UTF-8 编码
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        String decodedContent = URLDecoder.decode(content, StandardCharsets.UTF_8);
         Teacher teacher = teacherService.findTeacherByTeacherNum(teacherNum);
        Post post = new Post();
        post.setCourseId(courseId);
        post.setTeacherId(teacher.getId());
        post.setTitle(decodedTitle);
        post.setContent(decodedContent);
        post.setFavoNum(0L);
        post.setLikeNum(0L);
        postService.savePost(post);
        Optional<Course> course = courseService.findCourseById(courseId);
        if (course.isPresent()) {
            Long teacherId = course.get().getTeacher().getId();
            System.out.println("Teacher ID: " + teacherId);
            post.setTeacherId(teacherId);
        } else {
            System.out.println("Course not found.");
        }

        postService.savePost(post);

        responseMap.put("message", "帖子发送成功");
        return ResponseEntity.ok(responseMap);
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
    //老师删除帖子
    @DeleteMapping("/course/deletePost/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> deletePost(@PathVariable Long postId){
        Map<String, Object> responseMap = new HashMap<>();
        postService.deletePost(postId);
            responseMap.put("message","删除成功");
            return ResponseEntity.ok(responseMap);

    }

    //TODO: 老师删除评论
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
}

