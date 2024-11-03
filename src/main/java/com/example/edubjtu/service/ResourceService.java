package com.example.edubjtu.service;

import com.example.edubjtu.model.Resource;
import com.example.edubjtu.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    private final String uploadDir = "src/main/file"; // 存储文件的路径

    public void saveResource(Long courseId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());

        Resource resource = new Resource();
        resource.setCourseId(courseId);
        resource.setFilePath(filePath.toString());
        resource.setFileType(file.getContentType());

        resourceRepository.save(resource);
    }

    public List<Resource> getResourcesByCourseId(Long courseId) {
        return resourceRepository.findByCourse_CourseId(courseId);
    }


}
