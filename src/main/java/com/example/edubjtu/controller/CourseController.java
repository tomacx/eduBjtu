package com.example.edubjtu.controller;

import com.example.edubjtu.dto.CoursePost;
import com.example.edubjtu.dto.PostComment;
import com.example.edubjtu.dto.ResourceList;
import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Notification;
import com.example.edubjtu.model.Resource;
import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.repository.CourseRepository;
import com.example.edubjtu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private TeacherService teacherService;

    //课程通知--done
    @GetMapping("/course/notification")
    @ResponseBody
    public  ResponseEntity<Map<String,Object>> getCourseNotification(@RequestParam Long courseId){
        Map<String,Object> modelMap = new HashMap<>();
        List<Notification> notifications=notificationService.getNotificationsByCourseId(courseId);
        modelMap.put("notifications",notifications);
        return ResponseEntity.ok(modelMap);
    }

    //课程讨论--done
    @GetMapping("/course/discussion")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDiscussion(@RequestParam Long courseId) {
        System.out.println("Received courseId: " + courseId); // 打印 courseId
        Map<String, Object> modelMap = new HashMap<>();
        List<CoursePost> posts = postService.getPostsByCourseId(courseId);
        modelMap.put("posts", posts);
        return ResponseEntity.ok(modelMap);
    }

    //课程讨论详情--done
    @GetMapping("/course/discussionById")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDiscussionById(@RequestParam Long postId) {
        Map<String, Object> modelMap = new HashMap<>();
        List<CoursePost> coursePost=postService.getPostByPostId(postId);
        List<PostComment> postComments=commentService.getCommentByPostId(postId);
        modelMap.put("postDetial", coursePost);
        modelMap.put("postComments", postComments);
        return ResponseEntity.ok(modelMap);
    }

    //课程简介--done
    @GetMapping("/course/intro")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCourseIntro( @RequestParam Long courseId) {
        Course course = courseService.getCourseByCourseId(courseId);
        Map<String,Object> modelMap = new HashMap<>();
        modelMap.put("intro", course.getIntro());
        return ResponseEntity.ok(modelMap);
    }

    //课程资源列表--电子课件--done
    @GetMapping("/course/resource")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCourseResource( @RequestParam Long courseId) {
        Map<String,Object> modelMap = new HashMap<>();
        List<ResourceList> resourceList= resourceService.getCourseResourcesByCourseId(courseId);
        modelMap.put("resourceList",resourceList);
        return ResponseEntity.ok(modelMap);
    }

    //课程资源--电子课件下载--done
    @GetMapping("/course/downLoadResource")
    public ResponseEntity<Map<String ,Object>> getFileUrl(@RequestParam Long resourceId) {
        Optional<Resource> resource = resourceService.getResourceByResourceId(resourceId);
        Map<String ,Object> modelMap = new HashMap<>();
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
            return ResponseEntity.ok(modelMap);
        } else {
            modelMap.put("URL", "Not Found");
            return ResponseEntity.ok(modelMap);
        }
    }

    //课程资源--习题集list--done
    @GetMapping("/course/resource/workSet")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCourseWorkSet( @RequestParam Long courseId) {
        Map<String,Object> modelMap = new HashMap<>();
        List<ResourceList> resourceList= resourceService.getCourseWorkSetByCourseId(courseId);
        modelMap.put("resourceList",resourceList);
        return ResponseEntity.ok(modelMap);
    }

    //获取course calendar--done
    @GetMapping("/course/calendar")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCourseCalendar( @RequestParam Long courseId) {
            Optional<Resource> resource = resourceService.getCalendarByResourceId(courseId);
            Map<String ,Object> modelMap = new HashMap<>();
            if (resource.isPresent()) {
                String filePath = resource.get().getFilePath();
                // 替换路径中的 `src\main` 为 `static`，并将反斜杠替换为正斜杠
                String adjustedFilePath = filePath.replace("src\\main\\resources\\static\\", "")
                        .replace("\\", "/");

                // 构造完整的 URL
                String fileUrl = "http://localhost:8000/" + adjustedFilePath;
                modelMap.put("URL", fileUrl);
                String fileType = resource.get().getFileType(); // 获取文件类型信息
                modelMap.put("fileType", fileType);
                return ResponseEntity.ok(modelMap);
            } else {
                modelMap.put("URL", "Not Found");
                return ResponseEntity.ok(modelMap);
            }

    }
    //获取course outline
    @GetMapping("/course/outline")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCourseOutline( @RequestParam Long courseId) {
        Optional<Resource> resource = resourceService.getOutLineByCourseId(courseId);
        Map<String ,Object> modelMap = new HashMap<>();
        if (resource.isPresent()) {
            String filePath = resource.get().getFilePath();
            // 替换路径中的 `src\main` 为 `static`，并将反斜杠替换为正斜杠
            String adjustedFilePath = filePath.replace("src\\main\\resources\\static\\", "")
                    .replace("\\", "/");

            // 构造完整的 URL
            String fileUrl = "http://localhost:8000/" + adjustedFilePath;
            modelMap.put("URL", fileUrl);
            String fileType = resource.get().getFileType(); // 获取文件类型信息
            modelMap.put("fileType", fileType);
            return ResponseEntity.ok(modelMap);
        } else {
            modelMap.put("URL", "Not Found");
            return ResponseEntity.ok(modelMap);
        }
    }
    //获取课程下的教师信息
    @GetMapping("/course/teacherInfo")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCourseTeacherInfo( @RequestParam Long courseId) {
        Map<String,Object> modelMap = new HashMap<>();
        Course course = courseService.getCourseByCourseId(courseId);
        Teacher teacher = course.getTeacher();
        modelMap.put("teacher", teacher);
        modelMap.put("teacher_Info", course.getTeacherInfo());
        return ResponseEntity.ok(modelMap);
    }

    //设置teacherInfo
    @PostMapping("/course/setTeacherInfo")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> setTeacherInfo(@RequestParam Long courseId, @RequestParam String phoneNum,@RequestParam String content) {
        Course course = courseService.getCourseByCourseId(courseId);
        course.setTeacherInfo(content);
        courseService.updateCourse(course);
        Teacher teacher = course.getTeacher();
        teacher.setPhoneNum(phoneNum);
        teacherService.updateTeacher(teacher);
        Map<String,Object> modelMap = new HashMap<>();
        modelMap.put("message", "教师信息上传成功");
        return ResponseEntity.ok(modelMap);
    }
}
