package com.example.edubjtu.repository;

import com.example.edubjtu.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentNum(String studentNum);
    
    // 添加这个方法来检查学生记录是否存在
    boolean existsByStudentNum(String studentNum);

    @Query(value = "SELECT s.* FROM student s " +
                   "JOIN course_teacher_student cts ON s.id = cts.student_id " +
                   "WHERE cts.course_id = :courseId", nativeQuery = true)
    List<Student> findByCourseId(@Param("courseId") Long courseId);

}
