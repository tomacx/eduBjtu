package com.example.edubjtu.model;

import jakarta.persistence.*;
// 讨论区内的帖子
@Entity
public class Post {
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

    //getter and setter
    public Long getPostId(){
        return postId;
    }

    public void setPostId(Long postId){
        this.postId = postId;
    }

    public Long getCourseId(){
        return courseId;
    }

    public void setCourseId(Long courseId){
        this.courseId = courseId;
    }

    public Long getStudentId(){
        return studentId;
    }

    public void setStudentId(Long studentId){
        this.studentId = studentId;
    }

    public Long getTeacherId(){
        return teacherId;
    }

    public void setTeacherId(Long teacherId){
        this.teacherId = teacherId;
    }

    public Long getLikeNum(){
        return likeNum;
    }

    public void setLikeNum(Long likeNum){
        this.likeNum = likeNum;
    }

    public Long getFavoNum(){
        return favoNum;
    }

    public void setFavoNum(Long favoNum){
        this.favoNum = favoNum;
    }
    
    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

}
