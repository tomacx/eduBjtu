package com.example.edubjtu.service;
import com.example.edubjtu.model.Homework;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.repository.HomeworkRepository;
import com.example.edubjtu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@Service
public class HomeWorkService {
    @Autowired
    private HomeworkRepository homeworkRepository;

    private final String uploadDir = "src/main/homework";
    public List<Homework> getHomeworkByCourseId(Long courseId){
        return homeworkRepository.findByCourseId(courseId);
    };

    @Autowired
    private StudentRepository studentRepository;

    public Homework getHomeworkByHomeworkId(Long homeworkId) {
        return homeworkRepository.findByHomeworkId(homeworkId);
    }

    public void saveHomework(Long courseId, MultipartFile file) throws  IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir,fileName);
        Files.write(filePath, file.getBytes());

        List<Student> students = studentRepository.findByCourseId(courseId);
        for(Student student : students){
            Homework homework = new Homework();
            homework.setCourseId(courseId);
            homework.setStudentNum(Long.valueOf(student.getStudentNum()));
            homework.setContent(fileName);  //先将作业的名字设置为文件名
            homeworkRepository.save(homework);
        }
        //TODO:上传作业的时候还需要有对应的resourceId
    }


}
