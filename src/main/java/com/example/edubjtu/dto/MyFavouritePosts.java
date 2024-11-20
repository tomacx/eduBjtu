package com.example.edubjtu.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyFavouritePosts {
    private Long favoriteId;

    private String postTitle; // 帖子标题

    private String courseName;

    private Long likeNum;

    private Long favoNum;

    private String content;

    private String favoriteName;

    public MyFavouritePosts(Long id,String postTitle, String courseName, Long likeNum, Long favoNum, String content,String favoriteName )
    {
        this.favoriteId=id;
        this.postTitle = postTitle;
        this.courseName = courseName;
        this.likeNum = likeNum;
        this.favoNum = favoNum;
        this.content = content;
        this.favoriteName = favoriteName;
    }
}
