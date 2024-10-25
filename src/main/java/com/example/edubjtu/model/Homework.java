package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class Homework implements Serializable {
    @Getter
    @Id
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

    @Column(name = "resource_Id")
    private Long resourceId;


}
