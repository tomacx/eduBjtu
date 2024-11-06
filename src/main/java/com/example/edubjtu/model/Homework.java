package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "homework")
public class Homework implements Serializable {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 确保使用自动生成策略
    @Column(name = "homework_Id")  // 指定主键列
    private Long homeworkId;

    @Getter
    @Column(name = "course_Id")
    private Long courseId;

    @Column(name = "student_num")
    private Long studentNum;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "avg_grade")
    private Integer avgGrade;

    @Column(name = "mutual_grade")
    private Integer mutualGrade;

    @Column(name = "content")
    private String content;

    @Column(name = "student_homework")
    private String studentHomework;



}
