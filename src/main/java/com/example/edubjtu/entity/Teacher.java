package com.example.edubjtu.entity;

import java.io.Serializable;

/**
 * (Teacher)实体类
 *
 * @author ysx
 * @since 2024-10-17 12:17:02
 */
public class Teacher implements Serializable {
    private static final long serialVersionUID = 954531132079316248L;
/**
     * 主键
     */
    private Long id;
/**
     * 教师账号
     */
    private String teachernum;
/**
     * 姓名
     */
    private String name;
/**
     * 密码
     */
    private String password;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeachernum() {
        return teachernum;
    }

    public void setTeachernum(String teachernum) {
        this.teachernum = teachernum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

