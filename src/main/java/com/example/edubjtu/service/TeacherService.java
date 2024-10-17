package com.example.edubjtu.service;

import com.example.edubjtu.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Teacher)表服务接口
 *
 * @author ysx
 * @since 2024-10-17 12:17:02
 */
public interface TeacherService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Teacher queryById(Long id);

    /**
     * 分页查询
     *
     * @param teacher 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Teacher> queryByPage(Teacher teacher, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param teacher 实例对象
     * @return 实例对象
     */
    Teacher insert(Teacher teacher);

    /**
     * 修改数据
     *
     * @param teacher 实例对象
     * @return 实例对象
     */
    Teacher update(Teacher teacher);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
