package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class HomeworkReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "homework_id")
    private Long homeworkId;
    @Column(name = "reviewer_id")
    private Long reviewerId;
    @Column(name = "score")
    private double score;
    @Column(name = "comments")
    private String comments;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
