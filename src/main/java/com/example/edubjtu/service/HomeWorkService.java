package com.example.edubjtu.service;

import com.example.edubjtu.model.Homework;
import com.example.edubjtu.repository.HomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class HomeWorkService {
    @Autowired
    private HomeworkRepository homeworkRepository;

    public List<Homework> getHomeworkByCourseId(Long courseId){
        return homeworkRepository.findByCourseId(courseId);
    };


    public Homework getHomeworkByHomeworkId(Long homeworkId) {
        return homeworkRepository.findByHomeworkId(homeworkId);
    }

    public void saveHomework(Long courseId, Long studentNum, MultipartFile file) throws  IOException {
        Homework homework = new Homework();
        homework.setCourseId(courseId);
        homework.setStudentNum(studentNum);
        homework.setContent(new String(file.getBytes()));
        //TODO:上传作业的时候还需要有对应的resourceId
    }
}
