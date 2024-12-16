package com.example.edubjtu.repository;

import com.example.edubjtu.dto.MyFavoOthersFavo;
import com.example.edubjtu.dto.MyFavouritePosts;
import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // 收藏的帖子
    @Query("""
        SELECT new com.example.edubjtu.dto.MyFavouritePosts(p.postId,f.favoriteId,p.title, c.name, p.likeNum, p.favoNum, p.content, fi.favoriteName) 
        FROM Favorite f 
        JOIN Post p ON f.postId = p.postId 
        JOIN Course c ON c.courseId = p.courseId 
        JOIN FavoriteInfo fi on f.favouriteNum=fi.favouriteNum
        WHERE fi.favoriteCreaterId = :student_id AND f.favoOthersFavoriteNum IS NULL
""")
    List<MyFavouritePosts> findByStudentId(@Param("student_id") Long studentId);

    //收藏的收藏夹
    @Query("""
    SELECT new com.example.edubjtu.dto.MyFavoOthersFavo(
        f.favoOthersFavoriteNum,
        fi.favoriteName,
        s.name
    )
    FROM Favorite f
    JOIN FavoriteInfo fi ON f.favoOthersFavoriteNum = fi.favouriteNum
    JOIN Student  s ON s.id = fi.favoriteCreaterId
    JOIN FavoriteInfo f2 on f2.favouriteNum=f.favouriteNum
    WHERE f.postId IS NULL and f2.favoriteCreaterId= :student_id
""")
    List<MyFavoOthersFavo> findFavosByStudentId(@Param("student_id") Long studentId);

    @Query("SELECT f FROM Favorite f WHERE f.postId = :postId AND f.favouriteNum = :favouriteNum")
    Optional<Favorite> findByPostIdAndFavoriteNum(@Param("postId") Long postId, @Param("favouriteNum") String favouriteNum);

}
