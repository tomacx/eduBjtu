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

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "like_num")
    private Long likeNum;

    @Column(name = "favo_num")
    private Long favoNum;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;
}
