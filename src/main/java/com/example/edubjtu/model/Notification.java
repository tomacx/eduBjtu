package com.example.edubjtu.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;
    @Column(name = "teacher_id")
    private Long teacherId;
    @Column(name = "content")
    private String content;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "title")
    private String title;

    //Getters and Setters
    public Long getNotificationId(){
        return notificationId;
    }

    public void setNotificationId(Long notificationId){
        this.notificationId = notificationId;
    }

    public Long getTeacherId(){
        return teacherId;
    }

    public void setTeacherId(Long teacherId){
        this.teacherId = teacherId;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
