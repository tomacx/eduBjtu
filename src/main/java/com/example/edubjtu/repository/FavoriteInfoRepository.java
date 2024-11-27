package com.example.edubjtu.repository;


import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.model.FavoriteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//TODO:增加收藏夹的相关增删改查
@Repository
public interface FavoriteInfoRepository extends JpaRepository<FavoriteInfo, Long> {
    public FavoriteInfo findFavoriteInfoByFavoriteCreaterId(Long studentId);
}
