package com.example.edubjtu.service;

import com.example.edubjtu.model.Homework;
import com.example.edubjtu.repository.HomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeWorkService {
    @Autowired
    private HomeworkRepository homeworkRepository;

    public List<Homework> getHomeworkByCourseId(Long courseId){
        return homeworkRepository.findByCourseId(courseId);
    };
}
