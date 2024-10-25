package com.example.edubjtu.model;

import jakarta.persistence.*;

import java.io.Serializable;
@Entity
public class Homework implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "homeworkId")
    private Long homeworkId;

    @Column(name = "courseId")
    private Long courseId;

    @Column(name = "studentNum")
    private Long studentNum;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "avgGrade")
    private Integer avgGrade;

    @Column(name = "mutualGrade")
    private Integer mutualGrade;

    @Column(name = "content")
    private String content;

    @Column(name = "resourceId")
    private Long resourceId;

    public Long getId() {return homeworkId;}






}
