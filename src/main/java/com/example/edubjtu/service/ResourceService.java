package com.example.edubjtu.service;

import com.example.edubjtu.model.Resources;
import com.example.edubjtu.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        Resources resources = new Resources();
        resources.setCourseId(courseId);
        resources.setFilePath(filePath.toString());
        resources.setFileType(file.getContentType());

        resourceRepository.save(resources);
    }

    public List<Resources> getResourcesByCourseId(Long courseId) {
        return resourceRepository.findByCourse_CourseId(courseId);
    }

    public Path getResourceFilePath(Long courseId, Long resourceId) throws IOException {
        Resources resourcesEntity = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IOException("Resource not found"));

        if (!resourcesEntity.getCourseId().equals(courseId)) {
            throw new IOException("Resource does not belong to the specified course");
        }

        return Paths.get(resourcesEntity.getFilePath());
    }

}
