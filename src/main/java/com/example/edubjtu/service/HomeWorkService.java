package com.example.edubjtu.service;
import com.example.edubjtu.dto.studentHomeWork;
import com.example.edubjtu.model.Homework;
import com.example.edubjtu.model.Resource;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.repository.HomeworkRepository;
import com.example.edubjtu.repository.ResourceRepository;
import com.example.edubjtu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    public void saveStudentHomework(Long homeworkId,Long courseId, String studentContent, MultipartFile[] files) throws IOException {
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new IOException("作业未找到"));

        // 清除原有的学生作业内容 使重复上传覆盖前一次结果
        homework.setContent(null);
        // 存储作业文字内容
        homework.setContent(studentContent);

        // 处理文件资源
        for (MultipartFile file : files) {
            // 清除原有的学生作业附件 使重复上传覆盖前一次结果
            resourceService.deleteHomeworkResourceByStudent(homeworkId);
            // 保存每个文件作为资源（调用 ResourceService 保存文件信息）
            resourceService.saveHomeworkResourceByStudent(homeworkId, courseId,file); // 需要在这儿创建相应的文件资源记录
        }

        homeworkRepository.save(homework); // 保存作业
    }


    public Object getHomeworkByCourseIdAndStudentNum(Long courseId,String studentNum) {
        return homeworkRepository.findStudentHomeWorkByCourseIdAndStudentNum(courseId,studentNum);
    }
    //教师批阅作业
    public void gradeStudentHomework(Integer homeworkNum, String studentNum, Integer score) throws IOException {
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

    public List<Homework> getFirstHomeworkByCourseId(Long courseId) {
        return homeworkRepository.findFirstHomeworkByCourseId(courseId);
    }


    //TODO: 随机分配作业

    public List<Homework> HomeworkReviewsRandomly(Long courseId, Integer homeworkNum, String studentNum) {
        List<Homework> homeworks = homeworkRepository.findByCourseIdAndHomeworkNum(courseId,homeworkNum);
        List<Student> students = studentRepository.findByCourseId(courseId);


        List<Homework> reviewAssignments = new ArrayList<>();
        Random random = new Random();
        List<Student> students2 = new ArrayList<>();
        Set<Student> assignedReviewers = new HashSet<>();
        while (assignedReviewers.size() < 4) { // 假设每个作业需要3个评审

                Student randomStudent = students.get(random.nextInt(students.size()));

                Boolean flag = true;

                for(Student student : students2){
                    if(randomStudent.getStudentNum().equals(student.getStudentNum())){
                        flag = false;
                    }
                }
                if (flag && !randomStudent.getStudentNum().equals(studentNum)) {
                    assignedReviewers.add(randomStudent);
                    students2.add(randomStudent);
            }
        }
            for (Student reviewer : assignedReviewers) {
                for(Homework homework1 : homeworks){
                    if(reviewer.getStudentNum().equals(homework1.getStudentNum())){
                        reviewAssignments.add(homework1);
                    }
                }
            }
        return reviewAssignments;
    }
}
