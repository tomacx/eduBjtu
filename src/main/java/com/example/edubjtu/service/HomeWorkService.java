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
import java.util.Optional;

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

    public void saveHomework(Long courseId, MultipartFile file, Integer homeworkNum) throws  IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir,fileName);
        Files.write(filePath, file.getBytes());

        List<Student> students = studentRepository.findByCourseId(courseId);

        Homework homework = new Homework();
        homework.setCourseId(courseId);
        for(Student student : students){
            homework.setHomeworkNum(homeworkNum);
            homework.setStudentNum(Long.valueOf(student.getStudentNum()));
        }
        homework.setContent(fileName);  //先将作业的名字设置为文件名
        homeworkRepository.save(homework);
        //TODO:上传作业的时候还需要有对应的resourceId
    }

    public void saveStudentHomework(Long homeworkId,MultipartFile file) throws IOException {
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new IOException("作业未找到"));

        //将文件内容存储到 student_homework 字段中
        homework.setStudentHomework(file.getBytes());
        homeworkRepository.save(homework);
    }

    //教师批阅作业
    public void gradeStudentHomework(Integer homeworkNum, Long studentNum, Integer score) throws IOException {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("分数必须在0到100之间");
        }

        // 查找对应的作业
        Optional<Homework> optionalHomework = homeworkRepository.findByHomeworkNumAndStudentNum(homeworkNum, studentNum);
        if (optionalHomework.isPresent()) {
            Homework homework = optionalHomework.get();
            homework.setGrade(score); // 设置分数
            homeworkRepository.save(homework); // 保存更新
        } else {
            throw new IOException("未找到对应的作业");
        }
    }
}
