package com.example.edubjtu.service;

import com.example.edubjtu.model.Course;
import com.example.edubjtu.model.Resource;
import com.example.edubjtu.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ResourceService resourceService;

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        System.out.println("Courses: " + courses); // 调试输出
        return courses;
    }

    public Course getCourseByTeacherId(Long id){
        return courseRepository.findByTeacherId(id);
    }
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public void updateCourseInfo(Long id, String intro, String outline, String teacherInfo) {
        Course course = getCourseById(id);
        if (course != null) {
            course.setIntro(intro);
            course.setOutline(outline);
            course.setTeacherInfo(teacherInfo);
            courseRepository.save(course);
        }
    }

    public void saveResource(Long courseId, MultipartFile file) throws IOException {
        String uploadDir = "uploads/" + courseId;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File uploadFile = new File(dir, file.getOriginalFilename());
        file.transferTo(uploadFile);
    }


    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course getCourseByCourseId(Long courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return courseRepository.findCoursesByTeacherId(teacherId);
    }

    public List<Course> getCourseByStudentId(Long studentId){
        return courseRepository.findCoursesByStudentId(studentId);
    }

    public void saveOutLine(Long courseId, MultipartFile file) throws IOException {
        System.out.println("进入save outline");
        if(file!=null){
            resourceService.saveCourseOutLineByTeacher(courseId,file);
        } else {
            // 如果文件为空，输出提示
            System.out.println("没有上传文件");
        }
    }

    public void saveCalendar(Long courseId, MultipartFile file) throws IOException {
        if(file!=null){
             resourceService.saveCourseCalendarByTeacher(courseId,file);
        } else {
            // 如果文件为空，输出提示
            System.out.println("没有上传文件");
        }
    }

    public Optional<Course> findCourseById(Long courseId) {
      return   courseRepository.findById(courseId);
    }
}
