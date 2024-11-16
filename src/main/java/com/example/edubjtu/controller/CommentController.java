package com.example.edubjtu.controller;

import com.example.edubjtu.dto.MyComment;
import com.example.edubjtu.model.Comment;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.CommentService;
import com.example.edubjtu.service.CourseService;
import com.example.edubjtu.service.StudentService;
import com.example.edubjtu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CommentService commentService;

    //获取某一个用户收到的评论
    @GetMapping("/getComments")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getComments(@RequestParam String userNum){
        Map<String,Object> map = new HashMap<>();
        Student student = studentService.findStudentByStudentNum(userNum);
        if(student!=null){
            List<MyComment> commentList=commentService.getAllCommentsByStudentId(student.getId());
            map.put("commentList",commentList);
            return ResponseEntity.ok(map);
        }
        map.put("success",false);
        return ResponseEntity.ok(map);
    }
}
