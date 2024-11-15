package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "resource")
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = true)
    private Course course; // 假设你有 Course 实体类

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework_id", referencedColumnName = "homework_id", nullable = true)
    private Homework homework; // 假设你有 Homework 实体类

    @Column(name = "file_path", length = 255, nullable = true)
    private String filePath;

    @Column(name = "file_type", length = 50, nullable = true)
    private String fileType;

    @Column(name = "course_outline", nullable = true)
    private Integer courseOutline;

    @Column(name = "course_calendar", nullable = true)
    private Integer courseCalendar;

    @Column(name = "course_resource", nullable = true)
    private Integer courseResource;

    @Column(name = "homework_resource", nullable = true)
    private Integer homeworkResource;

    @Column(name = "course_workset",nullable = true)
    private Integer courseWorkset;

}
