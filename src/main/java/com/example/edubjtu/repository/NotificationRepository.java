package com.example.edubjtu.repository;

import com.example.edubjtu.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT n.* FROM notification n " +
            "JOIN notification_student ns ON n.notification_id = ns.notification_id " +
            "WHERE ns.student_id = :studentId", nativeQuery = true)
    List<Notification> findByStudentNum(Long studentId);
}
