package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "student")
public class Student implements Serializable {
    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "student_num")
    private String studentNum;
    private String password;


    public String getPasswordMask() {
        return "*******";
    }
}
