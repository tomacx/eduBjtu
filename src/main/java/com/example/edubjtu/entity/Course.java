package com.example.edubjtu.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (Course)实体类
 *
 * @author ysx
 * @since 2024-10-17 11:58:49
 */
@Getter
@Setter
public class Course implements Serializable {
    private static final long serialVersionUID = -95035421019465780L;
/**
     * 课程ID
     */
    private Long courseid;
/**
     * 课程介绍
     */
    private String intro;
/**
     * 课程大纲图片数据
     */
    private String outline;
/**
     * 课程日历图片数据
     */
    private String calendar;
/**
     * 教师信息
     */
    private String teacherinfo;
/**
     * 课程资源路径
     */
    private String resource;


    public Long getCourseid() {
        return courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getTeacherinfo() {
        return teacherinfo;
    }

    public void setTeacherinfo(String teacherinfo) {
        this.teacherinfo = teacherinfo;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

}

