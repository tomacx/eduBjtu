package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "course")
public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "name")
    private String name;

    @Column(name = "intro")
    private String intro;

    @Column(name = "outline")
    private String outline;

    @Column(name = "teacher_info")
    private String teacherInfo;

    @Column(name = "calendar")
    private String calendar;

    @Column(name = "resource")
    private String resource;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
