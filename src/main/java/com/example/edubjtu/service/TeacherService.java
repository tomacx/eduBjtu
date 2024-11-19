package com.example.edubjtu.service;

import com.example.edubjtu.model.Teacher;
import com.example.edubjtu.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher findTeacherByTeacherNum(String teacherNum) {
        return teacherRepository.findByTeacherNum(teacherNum);
    }

    public void updateTeacherPassword(Long teacherId, String newPassword) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher != null && newPassword != null && !newPassword.isEmpty()) {
            teacher.setPassword(newPassword); // 注意：实际应用中应该使用加密的密码
            teacherRepository.save(teacher);
        }
    }
    public Optional<Teacher> findTeacherById(Long teacherId){
        return teacherRepository.findById(teacherId);
    }
}
