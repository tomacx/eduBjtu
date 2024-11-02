package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "favorite")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "favourite_num")
    private String favouriteNum;

    @Column(name = "favo_others_favorite_num")
    private String favoOthersFavoriteNum;

}
