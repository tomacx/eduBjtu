package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "homework")
public class Homework implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homework_id")
    private Long homeworkId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "student_num")
    private Long studentNum;

    @Column(name = "grade") // 存储分数
    private Integer grade;

    @Column(name = "avg_grade")
    private Integer avgGrade;

    @Column(name = "mutual_grade")
    private Integer mutualGrade;

    @Column(name = "content")
    private String content;

//    @Column(name = "resource_id")
//    private Long resourceId;

    @Lob // 使用 LOB 类型存储大对象
    @Column(name = "student_homework")
    private byte[] studentHomework; // 存放学生上传的作业文档

    @Column(name = "homework_num") // 确保列名与数据库一致
    private Integer homeworkNum; // 存储作业次数
}
