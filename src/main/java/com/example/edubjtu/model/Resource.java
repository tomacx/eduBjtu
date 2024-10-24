package com.example.edubjtu.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId;
    
    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    // Getters and Setters
    public Long getCourseId(){
        return courseId;
    }

    public void setCourseId(Long courseId){
        this.courseId = courseId;
    }

    public String getFilePath(){
        return filePath;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getFileType(){
        return fileType;
    }

    public void setFileType(String fileType){
        this.fileType = fileType;
    }
    
    
}
