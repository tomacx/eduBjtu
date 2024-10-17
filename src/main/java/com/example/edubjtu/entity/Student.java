package com.example.edubjtu.entity;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * (Student)实体类
 *
 * @author ysx
 * @since 2024-10-17 20:02:15
 */
@Getter
@Setter
@Data
@TableName("student")
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {
    private static final long serialVersionUID = 530853714875704663L;
/**
     * 主键
     */
    private Long id;
/**
     * 姓名
     */
    private String name;
/**
     * 账号
     */
    private String studentnum;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentnum() {
        return studentnum;
    }

    public void setStudentnum(String studentnum) {
        this.studentnum = studentnum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

