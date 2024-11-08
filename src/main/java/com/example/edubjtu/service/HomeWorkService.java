package com.example.edubjtu.service;
import com.example.edubjtu.model.Homework;
import com.example.edubjtu.model.Resource;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.repository.HomeworkRepository;
import com.example.edubjtu.repository.ResourceRepository;
import com.example.edubjtu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class HomeWorkService {
    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceService resourceService;

    public List<Homework> getHomeworkByCourseId(Long courseId){
        return homeworkRepository.findByCourseId(courseId);
    };

    @Autowired
    private StudentRepository studentRepository;

    public Homework getHomeworkByHomeworkId(Long homeworkId) {
        return homeworkRepository.findByHomeworkId(homeworkId);
    }


    //老师布置作业
    public void saveHomework(Long courseId, int homeworkNum, Date deadline, String content, MultipartFile file) throws  IOException {
        if(file!=null){
            resourceService.saveHomeWorkResourceByTeacher(courseId,homeworkNum,file);
        } else {
            // 如果文件为空，输出提示
            System.out.println("没有上传文件，跳过作业资源保存步骤");
        }


        List<Student> students = studentRepository.findByCourseId(courseId);
        for(Student student : students){
            Homework homework = new Homework();
            homework.setCourseId(courseId);
            homework.setStudentNum(student.getStudentNum());
            homework.setContent(content);  //作业文字内容
            homework.setSubmissionDeadline(deadline);
            homework.setHomeworkNum(homeworkNum);
            homeworkRepository.save(homework);
        }
    }

    public void saveStudentHomework(Long homeworkId, String studentContent, MultipartFile[] files) throws IOException {
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new IOException("作业未找到"));

        // 存储作业文字内容
        homework.setStudentContent(studentContent);

        // 处理文件资源
        for (MultipartFile file : files) {
            // 保存每个文件作为资源（调用 ResourceService 保存文件信息）
            resourceService.saveHomeworkResourceByStudent(homeworkId, file); // 需要在这儿创建相应的文件资源记录
        }

        homeworkRepository.save(homework); // 保存作业
    }


    public Object getHomeworkByCourseIdAndStudentNum(Long courseId,String studentNum) {
        return homeworkRepository.findByCourseIdAndStudentNum(courseId,studentNum);
    }
}
