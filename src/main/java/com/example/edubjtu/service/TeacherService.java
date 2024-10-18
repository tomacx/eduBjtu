package com.example.edubjtu.service;

import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher findTeacherByTeacherNum(String teacherNum) {
        return teacherRepository.findByTeacherNum(teacherNum);
    }
}
