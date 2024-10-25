package com.example.edubjtu.repository;

import com.example.edubjtu.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByCourseId(@Param("courseId") Long courseId);

    Homework findByHomeworkId(@Param("homework_id")Long homeworkId);
}
