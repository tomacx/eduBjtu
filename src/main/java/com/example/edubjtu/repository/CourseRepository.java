package com.example.edubjtu.repository;

import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseId(Long courseId); // 使用 findByCourseId 方法

    List<Course> findByTeacher_Id(Long teacherId); // 使用findByTeacherId 方法

    List<Course> findByTeacher(Teacher teacher); // 使用findByTeacher 方法

    Course findByTeacherId(Long id);

     @Query("SELECT c FROM Course c JOIN CourseTeacherStudent cts ON c.courseId = cts.courseId WHERE cts.teacherId = :teacherId")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Long teacherId);


    @Query("SELECT c FROM Course c JOIN CourseTeacherStudent cts ON c.courseId = cts.courseId WHERE cts.studentId = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);
}
