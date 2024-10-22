package com.example.edubjtu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// 确保这里包含了控制器所在的包
public class EduBjtuApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduBjtuApplication.class, args);
    }

}
