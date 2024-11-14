package com.example.edubjtu.repository;

import com.example.edubjtu.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByCourseId(@Param("course_id") Long courseId);

    Homework findByHomeworkId(@Param("homework_id")Long homeworkId);

    Optional<Homework> findByHomeworkNumAndStudentNum(@Param("homework_num")Integer homeworkNum,@Param("student_num")Long studentNum);

    List<Homework> findByCourseIdAndStudentNum(@Param("course_id")Long courseId, @Param("student_num")String studentNum);
}
