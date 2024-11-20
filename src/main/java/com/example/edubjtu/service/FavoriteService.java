package com.example.edubjtu.service;

import com.example.edubjtu.dto.MyFavoOthersFavo;
import com.example.edubjtu.dto.MyFavouritePosts;
import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;


    public List<MyFavouritePosts> getFavoritesPostsByStudentId(Long id) {
        return favoriteRepository.findByStudentId(id);
    }

    public List<MyFavoOthersFavo> getMyFavOthersFavoByStudentId(Long id) {
        return favoriteRepository.findFavosByStudentId(id);
    }

    public boolean deleteFavoPostByFavoId(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
        return !favoriteRepository.existsById(favoriteId);
    }
}
