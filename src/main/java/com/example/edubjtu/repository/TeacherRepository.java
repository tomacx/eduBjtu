package com.example.edubjtu.repository;

import com.example.edubjtu.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByTeacherNum(String teacherNum);
}
