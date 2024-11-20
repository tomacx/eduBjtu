package com.example.edubjtu.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class studentHomeWork {

    private Long homeworkId;

    private Long courseId;

    private String studentNum;

    private Integer grade;

    private Integer avgGrade;

    private Integer mutualGrade;

    private String content;

    private Integer homeworkNum;

    private Date submissionDeadline;

    private String studentContent;

    private Boolean submitcheck;

    public studentHomeWork(Long homeworkId,Long courseId,String studentNum,Integer grade,Integer avgGrade,Integer mutualGrade,String content,Integer homeworkNum,Date submissionDeadline,String studentContent,Boolean submitcheck){
        this.homeworkId = homeworkId;
        this.courseId = courseId;
        this.studentNum = studentNum;
        this.grade = grade;
        this.avgGrade = avgGrade;
        this.mutualGrade = mutualGrade;
        this.content = content;
        this.homeworkNum = homeworkNum;
        this.submissionDeadline = submissionDeadline;
        this.studentContent = studentContent;
        this.submitcheck = submitcheck;

    }
}
