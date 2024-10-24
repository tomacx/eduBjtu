package com.example.edubjtu.model;

import jakarta.persistence.*;
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "post_id")
    private Long postId;

    //Getters and Setters
    public Long getFavoriteId(){
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId){
        this.favoriteId = favoriteId;
    }

    public Long getStudentId(){
        return studentId;
    }

    public void setStudentId(Long studentId){
        this.studentId = studentId;
    }

    public Long getPostId(){
        return postId;
    }

    public void setPostId(Long postId){
        this.postId = postId;
    }
}
