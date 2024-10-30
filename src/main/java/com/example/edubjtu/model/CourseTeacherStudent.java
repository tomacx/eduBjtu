package com.example.edubjtu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_teacher_student")
public class CourseTeacherStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne
    @JoinColumn(name="course_id", referencedColumnName = "course_id", insertable = false, updatable = false)
    private Course course;

    public CourseTeacherStudent(Course course) {
        this.course = course;
    }

    public CourseTeacherStudent() {

    }
}
