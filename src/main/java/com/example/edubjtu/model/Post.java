package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// 讨论区内的帖子
@Setter
@Getter
@Entity
@Table(name = "post")
public class Post {
    //getter and setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "student_id")//由学生创建
    private Long studentId;

    @Column(name = "teacher_id")//由老师创建
    private Long teacherId;

    @Column(name = "like_num")
    private Long likeNum;

    @Column(name = "favo_num")
    private Long favoNum;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    public Post(){

    }

    public Post(Long postId,Long courseId, Long studentId, Long teacherId,Long likeNum, Long favoNum, String content,String title){
        this.postId = postId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.likeNum = likeNum;
        this.favoNum = favoNum;
        this.title = title;
        this.content = content;
    }
}
