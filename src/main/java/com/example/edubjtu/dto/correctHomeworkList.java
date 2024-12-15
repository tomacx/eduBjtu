package com.example.edubjtu.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class correctHomeworkList {

    private Long homeworkId;

    private Long courseId;

    private String studentNum;

    private String studentName;

    private Integer grade;

    private Integer mutualGrade;

    private Integer homeworkNum;

    private int submitCheck;

    private boolean correctCheck=false;

    public correctHomeworkList (Long homeworkId, Long courseId, String studentNum, String studentName, Integer grade, Integer mutualGrade, Integer homeworkNum,int submitCheck){
        this.homeworkId=homeworkId;
        this.courseId=courseId;
        this.studentNum=studentNum;
        this.studentName=studentName;
        this.grade=grade;
        this.mutualGrade=mutualGrade;
        this.homeworkNum=homeworkNum;
        this.submitCheck=submitCheck;
        if(grade!=null) correctCheck=true;
    }
}
