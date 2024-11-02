package com.example.edubjtu.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyComment {
    private Long commentId;

    private Long postId;

    private int likeNum;

    private String content;

    private String  commentedNum;//被回复者的账号

    private String  commentedName;//被回复者的姓名

    private Long teacherId;

    private Long studentId;

    private String postTitle; // 帖子标题

    // 添加构造函数
    public MyComment(Long commentId, Long postId, int likeNum, String content,
                     String commentedNum, String commentedName, Long teacherId,
                     Long studentId, String postTitle) {
        this.commentId = commentId;
        this.postId = postId;
        this.likeNum = likeNum;
        this.content = content;
        this.commentedNum = commentedNum;
        this.commentedName = commentedName;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.postTitle = postTitle;
    }
}
