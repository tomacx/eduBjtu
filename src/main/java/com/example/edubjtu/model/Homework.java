package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "homework")
public class Homework implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 确保使用自动生成策略
    @Column(name = "homework_Id")  // 指定主键列
    private Long homeworkId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "student_num")
    private String studentNum;

    @Column(name = "grade") // 存储分数
    private Integer grade;

    @Column(name = "avg_grade")
    private Integer avgGrade;

    @Column(name = "mutual_grade")
    private Integer mutualGrade;

    @Column(name = "content")
    private String content;

    @Column(name = "homework_num")
    private Integer homeworkNum;

    @Column(name = "submission_deadline")
    @Temporal(TemporalType.TIMESTAMP)  // Using TIMESTAMP to store both date and time
    private Date submissionDeadline;

    @Column(name = "student_content")
    private String studentContent;

    @Column(name = "submit_check")
    private Integer submitCheck;
}
