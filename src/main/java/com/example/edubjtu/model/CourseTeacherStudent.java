package com.example.edubjtu.model;

import jakarta.persistence.*;

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

    //Getters and Setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getCourseId(){
        return courseId;
    }

    public void setCourseId(Long courseId){
        this.courseId = courseId;
    }

    public Long getTeacherId(){
        return teacherId;
    }

    public void setTeacherId(Long teacherId){
        this.teacherId = teacherId;
    }

    public Long getStudentId(){
        return studentId;
    }

    public void setStudentId(Long studentId){
        this.studentId = studentId;
    }
}
