package com.example.edubjtu.service;

import com.example.edubjtu.dto.ResourceList;
import com.example.edubjtu.model.Resource;
import com.example.edubjtu.repository.CourseRepository;
import com.example.edubjtu.repository.HomeworkRepository;
import com.example.edubjtu.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private HomeworkRepository homeworkRepository;
    private final String uploadCourseResourceDir = "src/main/resources/static/course"; // 存储文件的路径
    private final String uploadCourseWorkSetDir = "src/main/resources/static/courseWorkSet";
    private final String uploadHomeWorkRequirementDir = "src/main/resources/static/homeworkRequirement";
    private final String uploadHomeWorkStudentDir = "src/main/resources/static/homework";

    public List<Resource> getResourcesByCourseId(Long courseId) {
        return resourceRepository.findByCourse_CourseId(courseId);
    }

    public Path getResourceFilePath(Long courseId, Long resourceId) throws IOException {
        Resource resourcesEntity = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IOException("Resource not found"));

        if (!resourcesEntity.getCourse().equals(courseId)) {
            throw new IOException("Resource does not belong to the specified course");
        }

        return Paths.get(resourcesEntity.getFilePath());
    }

    //学生作业
//    public void saveResourceByHomeworkId(Long homeworkId, MultipartFile file) throws IOException{
//        // 创建并保存 Resource 实体
//        Resource resource = new Resource();
//        resource.setHomework(this.getHomeworkByHomeworkId(homeworkId)); // 关联作业
//        resource.setFilePath(saveFile(file)); // 保存文件路径
//        resource.setFileType(file.getContentType()); // 设置文件类型
//
//        resourceRepository.save(resource); // 保存资源记录
//    }
//
//    // 保存文件资源（可以在 service 中处理）
//    private void saveFileAsResource(Long homeworkId, MultipartFile file) throws IOException {
//        // 创建并保存 Resource 实体
//        Resource resource = new Resource();
//        resource.setHomework(this.getHomeworkByHomeworkId(homeworkId)); // 关联作业
//        resource.setFilePath(saveFile(file)); // 保存文件路径
//        resource.setFileType(file.getContentType()); // 设置文件类型
//
//        resourceRepository.save(resource); // 保存资源记录
//    }
//
//    // 保存文件的实际存储路径（假设存储在本地文件系统中）
//    private String saveFile(MultipartFile file) throws IOException {
//        String filePath = "../../studentHomeWork/" + file.getOriginalFilename();
//        File dest = new File(filePath);
//        file.transferTo(dest);
//        return filePath;
//    }
    //老师布置作业
    public void saveHomeWorkResourceByTeacher(Long courseId, int homeworkNum, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadHomeWorkRequirementDir,fileName);
        // 如果目录不存在，则创建目录
        Files.createDirectories(Path.of(uploadHomeWorkRequirementDir));
        Files.write(filePath, file.getBytes());
        Resource resources = new Resource();
        resources.setCourse(courseRepository.findByCourseId(courseId));
        resources.setFilePath(filePath.toString());
        resources.setFileType(file.getContentType());
        resources.setHomeworkResource(homeworkNum);
        resourceRepository.save(resources);
    }

    //学生上传完成的作业
    public void saveHomeworkResourceByStudent(Long homeworkId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadHomeWorkStudentDir,fileName);
        Files.createDirectories(Path.of(uploadHomeWorkStudentDir));
        Files.write(filePath, file.getBytes());
        Resource resources = new Resource();
        resources.setHomework(homeworkRepository.findByHomeworkId(homeworkId));
        resources.setFilePath(filePath.toString());
        resources.setFileType(file.getContentType());
        resourceRepository.save(resources);
    }

    //老师上传课程资源
    public void saveCourseResource(Long courseId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadCourseResourceDir, fileName);
        // 如果目录不存在，则创建目录
        Files.createDirectories(Path.of(uploadCourseResourceDir));
        Files.write(filePath, file.getBytes());

        Resource resources = new Resource();
        resources.setCourse(courseRepository.findByCourseId(courseId));
        resources.setFilePath(filePath.toString());
        resources.setFileType(file.getContentType());
        resources.setCourseResource(1);
        resourceRepository.save(resources);
    }

    public void saveCourseResources(Long courseId, MultipartFile[] file) throws IOException {
        // 处理文件资源
        for (MultipartFile f : file) {
            // 保存每个文件作为资源（调用 ResourceService 保存文件信息）
            this.saveCourseResource(courseId, f); // 需要在这儿创建相应的文件资源记录
        }
    }

    //老师上传课程习题集
    public void saveCourseWorkSet(Long courseId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadCourseWorkSetDir, fileName);
        // 如果目录不存在，则创建目录
        Files.createDirectories(Path.of(uploadCourseWorkSetDir));
        Files.write(filePath, file.getBytes());

        Resource resources = new Resource();
        resources.setCourse(courseRepository.findByCourseId(courseId));
        resources.setFilePath(filePath.toString());
        resources.setFileType(file.getContentType());
        resources.setCourseWorkset(1);
        resourceRepository.save(resources);
    }

    public void saveCourseWorkSets(Long courseId, MultipartFile[] file )throws IOException {
        for(MultipartFile f : file) {
            this.saveCourseWorkSet(courseId,f);
        }
    }

    //返回课件资源list
    public List<ResourceList> getCourseResourcesByCourseId(Long courseId) {
        int courseResource = 1;
        return resourceRepository.findByCourseIdAndCourseResource(courseId,courseResource);
    }
    //通过id返回具体资源
    public Optional<Resource> getResourceByResourceId(Long resourceId) {
        return resourceRepository.findById(resourceId);
    }
    //返回workset list
    public List<ResourceList> getCourseWorkSetByCourseId(Long courseId) {
        return resourceRepository.findByCourseIdAndCourseWorkSet(courseId,1);
    }

    //获取课程大纲
    public Optional<Resource> getOutLineByCourseId(Long courseId){
        return resourceRepository.findOutLineByCourse_CourseIdAndCourseOutline(courseId,1);
    }
    //获取课程时间表
    public Optional<Resource> getCalendarByResourceId(Long courseId) {
        return resourceRepository.findCalendarByCourse_CourseIdAndCourseCalendar(courseId,1);
    }

    public void saveCourseOutLineByTeacher(Long courseId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadCourseResourceDir, fileName);
        // 如果目录不存在，则创建目录
        Files.createDirectories(Path.of(uploadCourseResourceDir));
        Files.write(filePath, file.getBytes());

        Resource resources = new Resource();
        resources.setCourse(courseRepository.findByCourseId(courseId));
        resources.setFilePath(filePath.toString());
        resources.setFileType(file.getContentType());
        resources.setCourseOutline(1);
        resourceRepository.save(resources);
    }
}
