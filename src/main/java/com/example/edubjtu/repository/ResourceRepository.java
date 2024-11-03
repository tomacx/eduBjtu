package com.example.edubjtu.repository;

import com.example.edubjtu.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {


    List<Resource> findByCourse_CourseId(Long courseId);

}
