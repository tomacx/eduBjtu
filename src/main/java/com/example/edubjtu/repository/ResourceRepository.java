package com.example.edubjtu.repository;

import com.example.edubjtu.model.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resources, Long> {


    List<Resources> findByCourse_CourseId(Long courseId);

}
