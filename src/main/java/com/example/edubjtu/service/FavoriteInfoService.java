package com.example.edubjtu.service;


import com.example.edubjtu.model.FavoriteInfo;

//TODO:增加收藏夹相关的操作
public class FavoriteInfoService {
    public static FavoriteInfo createFavoriteInfo(String favoriteNum, String favoriteName, Long favoriteCreaterId){
        FavoriteInfo favoriteInfo = new FavoriteInfo();
        favoriteInfo.setFavouriteNum(favoriteNum);
        favoriteInfo.setFavoriteName(favoriteName);
        favoriteInfo.setFavoriteCreaterId(favoriteCreaterId);
        return favoriteInfo;
    }
}
