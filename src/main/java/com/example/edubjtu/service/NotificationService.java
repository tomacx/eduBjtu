package com.example.edubjtu.service;

import com.example.edubjtu.model.Notification;
import com.example.edubjtu.repository.NotificationRepository;
import org.hibernate.jdbc.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotification(){
        List<Notification> notifications = notificationRepository.findAll();
        return notifications;
    }

    public List<Notification> getNotificationsByStudentNum(Long studentId) {
        return notificationRepository.findByStudentNum(studentId);
    }

    // 通知的保存
    public boolean saveNotification(Long teacherId, String title, String content){
        try{
            Notification notification = new Notification();
            notification.setTeacherId(teacherId);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setCreateTime(LocalDateTime.now());

            notificationRepository.save(notification);
            return true;
        }catch(Exception e){
            System.out.println("False to save!");
            return false;
        }
    }

    public List<Notification> getNotificationsByCourseId(Long courseId) {
        return notificationRepository.findByCourseId(courseId);
    }
}
