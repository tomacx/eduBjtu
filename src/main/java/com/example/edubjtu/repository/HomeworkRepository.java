package com.example.edubjtu.repository;

import com.example.edubjtu.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByCourseId(@Param("course_id") Long courseId);

    Homework findByHomeworkId(@Param("homework_id")Long homeworkId);

    List<Homework> findByCourseIdAndStudentNum(@Param("course_id")Long courseId, @Param("student_num")String studentNum);
}
