package com.example.edubjtu.service;

import com.example.edubjtu.model.Student;
import com.example.edubjtu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student findStudentByStudentNum(String studentNum) {
        Student student = studentRepository.findByStudentNum(studentNum);
        if (student == null) {
            System.out.println("No student found with student number: " + studentNum);
        } else {
            System.out.println("Found student: " + student.getName());
        }
        return student;
    }

    public void updateStudentPassword(String studentNum, String newPassword) {
        Student student = findStudentByStudentNum(studentNum);
        if (student != null && newPassword != null && !newPassword.isEmpty()) {
            student.setPassword(newPassword); // 注意：实际应用中应该使用加密的密码
            studentRepository.save(student);
            logger.info("Password updated for student: {}", studentNum);
        } else {
            logger.warn("Failed to update password for student: {}", studentNum);
        }
    }

    public String getStudentName(String studentNum) {
        Student student = findStudentByStudentNum(studentNum);
        return student != null ? student.getName() : null;
    }

    public Student getStudentByStudentNum(String studentNum) {
        return studentRepository.findByStudentNum(studentNum);
    }

    public boolean studentExists(String studentNum) {
        boolean exists = studentRepository.existsByStudentNum(studentNum);
        System.out.println("Student with number " + studentNum + " exists: " + exists);
        return exists;
    }
    public List<Student> findStudentsByCourseId(Long courseId){
        return studentRepository.findByCourseId(courseId);
    }
}
