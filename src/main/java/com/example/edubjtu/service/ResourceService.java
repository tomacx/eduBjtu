package com.example.edubjtu.service;

import com.example.edubjtu.model.Resource;
import com.example.edubjtu.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public void saveResource(Long courseId, MultipartFile file) throws IOException {
        String filePath = "uploads/" + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        Resource resource = new Resource();
        resource.setCourseId(courseId);
        resource.setFilePath(filePath);
        resource.setFileType(file.getContentType());

        resourceRepository.save(resource);
    }

    public List<Resource> getResourcesByCourseId(Long courseId) {
        return resourceRepository.findByCourse_CourseId(courseId);
    }


}
