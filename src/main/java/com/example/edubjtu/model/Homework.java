package com.example.edubjtu.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

public class Homework implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "homeworkId")
    private Long homeworkId;

    @Column(name = "coureId")
    private Long coureId;

    @Column(name = "studentNum")
    private Long studentNum;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "avgGrade")
    private Integer avgGrade;

    @Column(name = "mutualGrade")
    private Integer mutualGrade;



    homeworkId  bigint        not null comment '主键'
    primary key,
    courseId    bigint        not null comment '外键, 关联到课程',
    studentNum  varchar(50)   not null comment '学生学号，关联到学生',
    grade       decimal(5, 2) null comment '作业评分',
    avgGrade    decimal(5, 2) null comment '平均评分',
    mutualGrade decimal(5, 2) null comment '互评成绩',
    content


}
