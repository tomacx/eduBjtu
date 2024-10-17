package com.example.edubjtu.service;

import com.example.edubjtu.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Course)表服务接口
 *
 * @author ysx
 * @since 2024-10-17 11:58:53
 */
public interface CourseService {

    /**
     * 通过ID查询单条数据
     *
     * @param courseid 主键
     * @return 实例对象
     */
    Course queryById(Long courseid);

    /**
     * 分页查询
     *
     * @param course 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Course> queryByPage(Course course, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param course 实例对象
     * @return 实例对象
     */
    Course insert(Course course);

    /**
     * 修改数据
     *
     * @param course 实例对象
     * @return 实例对象
     */
    Course update(Course course);

    /**
     * 通过主键删除数据
     *
     * @param courseid 主键
     * @return 是否成功
     */
    boolean deleteById(Long courseid);

}
