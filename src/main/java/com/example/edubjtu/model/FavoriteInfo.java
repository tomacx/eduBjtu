package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "favoriteinfo")
public class FavoriteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long favoriteInfoId;

    @Column(name = "favorite_num", nullable = false)
    private String favouriteNum; // 收藏夹编号

    @Column(name = "favorite_name", nullable = false)
    private String favoriteName; // 收藏夹名称

    @Column(name = "favorite_creater_id", nullable = false)
    private Long favoriteCreaterId; // 创建者的学生ID



}
