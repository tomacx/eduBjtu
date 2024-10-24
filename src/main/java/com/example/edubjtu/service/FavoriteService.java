package com.example.edubjtu.service;

import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getFavoritesByStudentId(Long studentId) {
        return favoriteRepository.findByStudentId(studentId);
    }
}
