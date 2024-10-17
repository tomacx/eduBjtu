package com.example.edubjtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.edubjtu.dao.StudentDao;
import com.example.edubjtu.dto.LoginDto;
import com.example.edubjtu.entity.Student;
import com.example.edubjtu.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * (Student)表服务实现类
 *
 * @author zoupeng
 * @since 2024-10-17 20:02:15
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentDao studentDao;

    @Override
    public Student login(LoginDto dto) throws Exception {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.in("studentNum",dto.getUsername());
        Student student = studentDao.selectOne(wrapper);
        if(student == null)throw new Exception("用户名或密码错误");
        if(!student.getPassword().equals(dto.getPassword()))throw new Exception("用户名或密码错误");
        return student;
    }

    @Override
    public Student getStudentById(int id) throws Exception {
        return studentDao.selectById(id);
    }

    @Override
    public Student getStudentByStudentNum(String studentnum) throws Exception {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentnum", studentnum);
        return studentDao.selectOne(queryWrapper);
    }
}
