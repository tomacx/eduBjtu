package com.example.edubjtu.service;

import com.example.edubjtu.dto.MyFavoOthersFavo;
import com.example.edubjtu.dto.MyFavouritePosts;
import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.repository.FavoriteRepository;
import com.example.edubjtu.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private PostRepository postRepository;

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

    public void addFavoritePost(Long postId, String favoriteNum ) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("帖子不存在"));

        //创建新的收藏信息
        Favorite favorite = new Favorite();
        favorite.setPostId(postId);
        favorite.setFavouriteNum(favoriteNum);
        favoriteRepository.save(favorite);
    }
}
