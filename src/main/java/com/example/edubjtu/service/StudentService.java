package com.example.edubjtu.service;


import com.example.edubjtu.dto.LoginDto;
import com.example.edubjtu.entity.Student;

/**
 * (Student)表服务接口
 *
 * @author zoupeng
 * @since 2024-10-17 20:02:15
 */
public interface StudentService {
    Student login(LoginDto dto) throws Exception;

    Student getStudentById(int id) throws Exception;

    Student getStudentByStudentNum(String studentnum) throws Exception;

}
