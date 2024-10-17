package com.example.edubjtu.service.impl;

import com.example.edubjtu.dao.StudentDao;
import com.example.edubjtu.entity.Student;
import com.example.edubjtu.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (Student)表服务实现类
 *
 * @author ysx
 * @since 2024-10-17 12:16:45
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentDao studentDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Student queryById(Long id) {
        return this.studentDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param student 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Student> queryByPage(Student student, PageRequest pageRequest) {
        long total = this.studentDao.count(student);
        return new PageImpl<>(this.studentDao.queryAllByLimit(student, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    @Override
    public Student insert(Student student) {
        this.studentDao.insert(student);
        return student;
    }

    /**
     * 修改数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    @Override
    public Student update(Student student) {
        this.studentDao.update(student);
        return this.queryById(student.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.studentDao.deleteById(id) > 0;
    }
}
