package com.example.edubjtu.model;

import jakarta.persistence.*;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noteId")
    private Long noteId;

    @Column(name = "studentId")
    private Long studentId;

    @Column(name = "courseId")
    private Long courseId;

    @Column(name = "content")
    private String  content;

    // Get and Set
    public Long getNoteId(){
        return noteId;
    }

    public void setNoteId(Long noteId){
        this.noteId = noteId;
    }

    public Long getStudentId(){
        return studentId;
    }

    public void setStudentId(Long studentId){
        this.studentId = studentId;
    }

    public Long getCourseId(){
        return courseId;
    }

    public void setCourseId(Long courseId){
        this.courseId = courseId;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

}
