package com.example.edubjtu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.edubjtu.dao")
public class EduBjtuApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduBjtuApplication.class, args);
    }

}
