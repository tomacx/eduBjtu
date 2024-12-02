package com.example.edubjtu.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface HomeworkReviewRespository extends JpaRepository<com.example.edubjtu.model.HomeworkReview, Long> {

}
