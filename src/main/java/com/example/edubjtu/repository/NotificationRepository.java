package com.example.edubjtu.repository;

import com.example.edubjtu.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT n.* FROM notification n " +
            "JOIN course_teacher_student cts ON n.teacher_id = cts.teacher_id " +
            "WHERE cts.student_id = :studentId", nativeQuery = true)
    List<Notification> findByStudentNum(Long studentId);
}
