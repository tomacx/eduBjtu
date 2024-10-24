package com.example.edubjtu.repository;

import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseId(Long courseId); // 使用 findByCourseId 方法

    List<Course> findByTeacher_Id(Long teacherId); // 使用findByTeacherId 方法

    List<Course> findByTeacher(Teacher teacher); // 使用findByTeacher 方法

    Course findByTeacherId(Long id);
}
