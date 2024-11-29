package com.example.edubjtu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoursePost {
    private Long postId;

    private Long courseId;

    private String studentName;

    private Long likeNum;

    private Long favoNum;

    private String content;

    private String title;

    public CoursePost(Long postId,Long courseId, String studentName, Long likeNum, Long favoNum, String content, String title){
        this.postId=postId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.likeNum = likeNum;
        this.favoNum = favoNum;
        this.content = content;
        this.title = title;
    }

    public CoursePost() {

    }
}
