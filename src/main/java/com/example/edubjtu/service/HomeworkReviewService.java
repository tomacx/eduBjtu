package com.example.edubjtu.service;

import com.example.edubjtu.model.HomeworkReview;
import com.example.edubjtu.repository.HomeworkReviewRespository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class HomeworkReviewService {
    @Autowired
    private HomeworkReviewRespository homeworkReviewRespository;

    public void submitReview(Long homeworkId, Long reviewerId,Double score,String comments){
        HomeworkReview review = new HomeworkReview();
        review.setHomeworkId(homeworkId);
        review.setReviewerId(reviewerId);
        review.setScore(score);
        review.setComments(comments);
        review.setCreatedAt(LocalDateTime.now());
        homeworkReviewRespository.save(review);
    }
}
