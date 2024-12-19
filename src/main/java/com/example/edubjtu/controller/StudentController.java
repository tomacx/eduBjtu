package com.example.edubjtu.controller;

import com.example.edubjtu.model.*;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

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

    @Autowired
    private CommentService commentService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private FavoriteInfoService favoriteInfoService;

    @Autowired
    private HomeworkReviewService homeworkReviewService;

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

    //homepage课程获取
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

    //homepage所有通知获取
    @GetMapping("/getNotification")
    public ResponseEntity<Map<String, Object>> getNotification(@RequestParam("studentNum") String studentNum) {
        Student student = studentService.findStudentByStudentNum(studentNum);
        List<Notification> notifications = notificationService.getNotificationsByStudentNum(student.getId());
        Map<String,Object> modelMap = new HashMap<>();
        modelMap.put("notifications", notifications);
        // 统计通知的数量并返回
        int notificationNum = notifications.size(); // 获取通知的数量
        modelMap.put("notificationNum", notificationNum); // 将数量放入 modelMap
        return ResponseEntity.ok(modelMap);
    }

    //学生获取所有作业--done
    @GetMapping("/course/homework")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHomework(@RequestParam Long courseId, @RequestParam String studentNum) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("homeworkList",homeworkService.getHomeworkByCourseIdAndStudentNum(courseId,studentNum));
        return ResponseEntity.ok(modelMap);
    };

    //增加学生端上传作业的功能--done
    @PostMapping("course/homework/upload")
    public ResponseEntity<Map<String, Object>> uploadStudentHomework(@RequestParam Long homeworkId,
                                                                     @RequestParam Long courseId,
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
        homeworkService.saveStudentHomework(homeworkId,courseId, studentContent, files);

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


    // 返回作业附件
    @GetMapping("/course/homework/attachments")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> attachements(@RequestParam Long courseId,@RequestParam Long homeworkNum) {
        Map<String, Object> modelMap = new HashMap<>();
        Optional<com.example.edubjtu.model.Resource> resource = homeworkService.getHomeworkAttachement(courseId,homeworkNum);
        if (resource.isPresent()) {
            String filePath = resource.get().getFilePath();

            //src\main\resources\static\course\Session_0_Requirements.pptx
            // 替换路径中的 `src\main` 为 `static`，并将反斜杠替换为正斜杠
            String adjustedFilePath = filePath.replace("src\\main\\resources\\static\\", "")
                    .replace("\\", "/");

            // 构造完整的 URL
            String fileUrl = "http://localhost:8000/" + adjustedFilePath;
            modelMap.put("URL", fileUrl);
            String fileType = resource.get().getFileType(); // 获取文件类型信息
            modelMap.put("fileType", fileType);
            modelMap.put("resourceId", resource.get().getId());
            return ResponseEntity.ok(modelMap);
        } else {
            modelMap.put("URL", "Not Found");
            return ResponseEntity.ok(modelMap);
        }
    }
    //删除收藏的post--done
    @DeleteMapping("/deleteCollectionOfPost/{favoriteId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCollectionOfPost(@PathVariable("favoriteId")Long favoriteId){
        Map<String, Object> responseMap = new HashMap<>();
        if(favoriteService.deleteFavoPostByFavoId(favoriteId)){
            responseMap.put("message","取消收藏成功");
        }else {
            responseMap.put("message","取消收藏失败");
        }
        return ResponseEntity.ok(responseMap);

    }

    //学生保存笔记--done
    @PostMapping("/saveNote")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveNote(@RequestParam("note_title") String title,
                                                        @RequestParam("content") String content,
                                                        @RequestParam("studentNum") String studentNum){
        Map<String, Object> responseMap = new HashMap<>();
        // 解码标题和内容
        try {
            // URL 解码，使用 UTF-8 编码
            String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
            String decodedContent = URLDecoder.decode(content, StandardCharsets.UTF_8);

            // 查找学生信息
            Student student = studentService.findStudentByStudentNum(studentNum);

            // 保存笔记（传入解码后的标题和内容）
            noteService.saveNote(decodedTitle, decodedContent, student.getId());

            // 返回成功信息
            responseMap.put("message", "笔记保存成功");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            // 解码失败时的处理
            responseMap.put("message", "解码失败，请检查输入");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }
    }
    //删除笔记--done
    @DeleteMapping("/deleteNote/{noteId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteNote(@PathVariable("noteId")Long noteId){
        Map<String, Object> responseMap = new HashMap<>();
        if(noteService.deleteNoteByNoteId(noteId)){
            responseMap.put("message","删除笔记成功");
        }else {
            responseMap.put("message","删除笔记失败");
        }
        return ResponseEntity.ok(responseMap);

    }
    // 增加学生端下载课程资源的功能--done
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

    //发送帖子--done
    @PostMapping("/setPost")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendPost(@RequestParam("courseId") Long courseId,
                                                        @RequestParam("title") String title,
                                                        @RequestParam("content") String content,
                                                        @RequestParam("studentNum") String studentNum) {
        Map<String, Object> responseMap = new HashMap<>();

        // URL 解码，使用 UTF-8 编码
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        String decodedContent = URLDecoder.decode(content, StandardCharsets.UTF_8);

        // 查询学生信息
        Student student = studentService.findStudentByStudentNum(studentNum);
        if (student == null) {
            responseMap.put("error", "学生信息未找到");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }

        // 创建帖子对象
        Post post = new Post();
        post.setCourseId(courseId);
        post.setStudentId(student.getId());
        post.setTitle(decodedTitle);
        post.setContent(decodedContent);
        post.setFavoNum(0L);
        post.setLikeNum(0L);

        // 查找课程并获取教师信息
        Optional<Course> course = courseService.findCourseById(courseId);
        if (course.isPresent()) {
            if (course.get().getTeacher() != null) {
                Long teacherId = course.get().getTeacher().getId();
                System.out.println("Teacher ID: " + teacherId);
                post.setTeacherId(teacherId); // 确保教师信息不为空
            } else {
                // 如果课程没有教师，返回错误信息
                responseMap.put("error", "该课程没有分配教师");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
            }
        } else {
            // 如果课程不存在
            responseMap.put("error", "课程未找到");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }

        // 保存帖子信息
        postService.savePost(post);

        // 返回成功消息
        responseMap.put("message", "帖子发送成功");
        return ResponseEntity.ok(responseMap);
    }


    //发送评论--done
    @PostMapping("/post/setComment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendComment(@RequestParam Long postId,
                                                           @RequestParam String studentNum,
                                                           @RequestParam("content") String content
                                                           ) {
        Map<String, Object> responseMap = new HashMap<>();
        Post post = postService.getPostById(postId);
        String deContent =  URLDecoder.decode(content, StandardCharsets.UTF_8);
        //由后端判断回复的帖子是老师的还是学生的
        //评论的学生
        Student student = studentService.findStudentByStudentNum(studentNum);
        if (post.getStudentId()!=null) {
            Optional<Student> commentedStudent = studentService.findStudentById(post.getStudentId());
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setLikeNum(0);
            comment.setCommentedNum(commentedStudent.get().getStudentNum());
            comment.setContent(deContent);
            comment.setStudentId(student.getId());
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
                comment.setStudentId(student.getId());
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

    //TODO:学生给评论进行回复 -- 已统一由回复接口完成
    @PostMapping("/comment/{commentId}/reply")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> replyToComment(@PathVariable Long commentId,
                                                              @RequestParam("content") String content,
                                                              HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student == null) {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }

        Comment reply = new Comment();
        //增加一个搜索回复的评论的人的学号
        Comment comment = commentService.getCommentById(commentId);
        if(comment.getStudentId() != null) {
            Optional<Student> student1 = studentService.findStudentById(comment.getStudentId());
            reply.setCommentedNum(student1.get().getStudentNum());

        }else if(comment.getTeacherId() != null) {
            Optional<Teacher> teacher1 = teacherService.findTeacherById(comment.getTeacherId());
            reply.setCommentedNum(teacher1.get().getTeacherNum());
        }
        reply.setPostId(comment.getPostId());
        reply.setStudentId(student.getId());
        reply.setContent(content);
        commentService.saveComment(reply);

        responseMap.put("message", "回复发送成功");
        responseMap.put("replyId", reply.getCommentId());
        return ResponseEntity.ok(responseMap);
    }
    //TODO:增加学生端对帖子、点赞、收藏的功能
    // 增加学生端收藏帖子的功能
    @PostMapping("/post/{postId}/favorite")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> favoritePost(@PathVariable Long postId,
                                                            @RequestParam("favoriteNum") String favoriteNum,
                                                            HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student == null) {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }

        try {
            favoriteService.addFavoritePost(postId,favoriteNum);
            responseMap.put("message", "收藏成功");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            responseMap.put("error", "收藏失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }
    //TODO:增加学生端对帖子点赞的功能
    @PostMapping("/post/{postId}/like")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable Long postId, HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student == null) {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }

        try {
            postService.likePost(postId);
            responseMap.put("message", "点赞成功");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            responseMap.put("error", "点赞失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    //TODO:增加学生端删除自己发送的帖子的功能
    @DeleteMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long postId, HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student == null) {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }

        Post post = postService.getPostById(postId);
        if (post == null || !post.getStudentId().equals(student.getId())) {
            responseMap.put("error", "无权删除此帖子或帖子不存在");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap);
        }

        postService.deletePost(postId);
        responseMap.put("message", "帖子删除成功");
        return ResponseEntity.ok(responseMap);
    }
    //TODO：增加收藏他人帖子的功能
    @PostMapping("/favoriteInfo/{favoriteNumOthers}/favorite")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> favoriteOthers(@PathVariable String favoriteNumOthers,
                                                              @RequestParam("favoriteNum") String favoriteNum,
                                                              HttpSession session){
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student == null) {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }
        try{
            favoriteService.addFavoriteInfo(favoriteNum,favoriteNumOthers);
            responseMap.put("message", "收藏他人收藏夹成功");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            responseMap.put("error", "收藏失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    //TODO:作业互评功能
    @PostMapping("/homework/{homeworkId}/review")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> reviewHomework(@PathVariable Long homeworkId,
                                                             @RequestParam("score") Double score,
                                                             @RequestParam("comments") String comments,
                                                             HttpSession session) {
        Map<String, Object> responseMap = new HashMap<>();
        Student student = (Student) session.getAttribute("loggedInStudent");
        if (student == null) {
            responseMap.put("error", "未登录，请重新登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
        }

        homeworkReviewService.submitReview(homeworkId, student.getId(), score, comments);
        responseMap.put("message", "作业评审成功");
        return ResponseEntity.ok(responseMap);
    }

    //TODO:获取随机互评的作业
    @GetMapping("/homework/{courseId}/{homeworkNum}/homeworkReviews")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> assignReviews(@PathVariable Long courseId,
                                                                   @PathVariable Integer homeworkNum,
                                                                   @RequestParam String studentNum) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            List<Map<String,Object>> modelList = new ArrayList<>();
            Map<String ,Object> modelMap1 = new HashMap<>();
            Map<String ,Object> modelMap2 = new HashMap<>();
            Map<String ,Object> modelMap3 = new HashMap<>();
            List<Homework> assignments = homeworkService.HomeworkReviewsRandomly(courseId,homeworkNum,studentNum);
            List<com.example.edubjtu.model.Resource> resources = new ArrayList<>();
            int number = 1;
            for(Homework homework : assignments){
                com.example.edubjtu.model.Resource re = resourceService.getResourceByHomeworkId(homework.getHomeworkId());
                resources.add(re);
                System.out.println(re.getFilePath());
                if(number == 1) {
                    modelMap1.put("homeworkId", homework.getHomeworkId());
                    modelMap1.put("content", homework.getContent());
                    number++;
                }else if(number == 2){
                    modelMap2.put("homeworkId", homework.getHomeworkId());
                    modelMap2.put("content", homework.getContent());
                    number++;
                }else if(number == 3){
                    modelMap3.put("homeworkId", homework.getHomeworkId());
                    modelMap3.put("content", homework.getContent());
                    number++;
                }
                System.out.println(homework.getHomeworkId());
                System.out.println(homework.getContent());
            }
            System.out.println("评审任务分配成功");

            number = 1;
            for(com.example.edubjtu.model.Resource resource : resources) {
                String filePath = resource.getFilePath();
                // 替换路径中的 `src\main` 为 `static`，并将反斜杠替换为正斜杠
                String adjustedFilePath = filePath.replace("src\\main\\resources\\static\\", "")
                        .replace("\\", "/");

                // 构造完整的 URL
                String fileUrl = "http://localhost:8000/" + adjustedFilePath;
                if(number == 1) {
                    modelMap1.put("URL", fileUrl);
                    String fileType = resource.getFileType(); // 获取文件类型信息
                    modelMap1.put("fileType", fileType);
                    System.out.println("fileType is:" + fileType);
                    modelList.add(modelMap1);
                    number++;
                }else if(number == 2){
                    modelMap2.put("URL", fileUrl);
                    String fileType = resource.getFileType(); // 获取文件类型信息
                    modelMap2.put("fileType", fileType);
                    System.out.println("fileType is:" + fileType);
                    modelList.add(modelMap2);
                    number++;
                }else if(number == 3){
                    modelMap3.put("URL", fileUrl);
                    String fileType = resource.getFileType(); // 获取文件类型信息
                    modelMap3.put("fileType", fileType);
                    System.out.println("fileType is:" + fileType);
                    modelList.add(modelMap3);
                    number++;
                }
            }
            return ResponseEntity.ok(modelList);
        } catch (Exception e) {
            responseMap.put("error", "评审任务分配失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((List<Map<String, Object>>) responseMap);
        }
    }
}
