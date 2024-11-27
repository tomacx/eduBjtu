package com.example.edubjtu.service;


import com.example.edubjtu.model.FavoriteInfo;
import com.example.edubjtu.repository.FavoriteInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//TODO:增加收藏夹相关的操作
@Service
public class FavoriteInfoService {

    @Autowired
    private FavoriteInfoRepository favoriteInfoRepository;
    public FavoriteInfo findByCreaterId(Long studentId){
        return favoriteInfoRepository.findFavoriteInfoByFavoriteCreaterId(studentId);
    }

    //创建一个默认的收藏夹
    public void CreateFavoriteInfo(Long favoriteCreatedId){
        FavoriteInfo favoriteInfo = new FavoriteInfo();
        favoriteInfo.setFavoriteCreaterId(favoriteCreatedId);
        favoriteInfo.setFavoriteName("默认收藏夹");
        favoriteInfo.setFavouriteNum(favoriteCreatedId.toString());
        favoriteInfoRepository.save(favoriteInfo);
    }
    public static FavoriteInfo createFavoriteInfo(String favoriteNum, String favoriteName, Long favoriteCreaterId){
        FavoriteInfo favoriteInfo = new FavoriteInfo();
        favoriteInfo.setFavouriteNum(favoriteNum);
        favoriteInfo.setFavoriteName(favoriteName);
        favoriteInfo.setFavoriteCreaterId(favoriteCreaterId);
        return favoriteInfo;
    }
}
