package com.example.edubjtu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostComment {

    private Long commentId;

    private Long postId;

    private int likeNum;

    private String content;

    private String  commentedNum;

    private Long teacherId;

    private Long studentId;

    private String commentedName;

    private String studentName;

    private String teacherName;

    public PostComment(Long commentId,Long postId,int likeNum,String content,String  commentedNum
    ,Long teacherId,Long studentId,String commentedName, String studentName, String teacherName){
        this.commentId = commentId;
        this.postId = postId;
        this.likeNum = likeNum;
        this.content = content;
        this.commentedNum = commentedNum;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.commentedName = commentedName;
        this.studentName = studentName;
        if(studentName!=null){
            this.teacherName = null;
        }else
        {
            this.teacherName = teacherName;
        }
    }

    public PostComment() {

    }
}
