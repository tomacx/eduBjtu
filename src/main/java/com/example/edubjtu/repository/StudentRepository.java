package com.example.edubjtu.repository;

import com.example.edubjtu.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentNum(String studentNum);
    
    // 添加这个方法来检查学生记录是否存在
    boolean existsByStudentNum(String studentNum);
}
