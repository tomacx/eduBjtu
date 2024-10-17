package com.example.edubjtu.service.impl;

import com.example.edubjtu.dao.CourseDao;
import com.example.edubjtu.entity.Course;
import com.example.edubjtu.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (Course)表服务实现类
 *
 * @author ysx
 * @since 2024-10-17 11:58:54
 */
@Service("courseService")
public class CourseServiceImpl implements CourseService {
    @Resource
    private CourseDao courseDao;

    /**
     * 通过ID查询单条数据
     *
     * @param courseid 主键
     * @return 实例对象
     */
    @Override
    public Course queryById(Long courseid) {
        return this.courseDao.queryById(courseid);
    }

    /**
     * 分页查询
     *
     * @param course 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Course> queryByPage(Course course, PageRequest pageRequest) {
        long total = this.courseDao.count(course);
        return new PageImpl<>(this.courseDao.queryAllByLimit(course, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param course 实例对象
     * @return 实例对象
     */
    @Override
    public Course insert(Course course) {
        this.courseDao.insert(course);
        return course;
    }

    /**
     * 修改数据
     *
     * @param course 实例对象
     * @return 实例对象
     */
    @Override
    public Course update(Course course) {
        this.courseDao.update(course);
        return this.queryById(course.getCourseid());
    }

    /**
     * 通过主键删除数据
     *
     * @param courseid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long courseid) {
        return this.courseDao.deleteById(courseid) > 0;
    }
}
