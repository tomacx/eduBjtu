package com.example.edubjtu.controller;

import com.example.edubjtu.dto.MyComment;
import com.example.edubjtu.dto.MyFavoOthersFavo;
import com.example.edubjtu.dto.MyFavouritePosts;
import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.FavoriteService;
import com.example.edubjtu.service.PostService;
import com.example.edubjtu.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/favourite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PostService postService;

    @Autowired
    StudentService studentService;

    //显示收藏列表
    @GetMapping("/getFavourite")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> listFavorites(HttpSession session, @RequestParam String studentNum) {
        Map<String,Object> map = new HashMap<>();
        Student student = studentService.findStudentByStudentNum(studentNum);
        if(student!=null){
            //获取收藏的posts
           List<MyFavouritePosts> postList  = favoriteService.getFavoritesPostsByStudentId(student.getId());
           map.put("postList",postList);
           //获取收藏的别人的收藏夹
           List<MyFavoOthersFavo> othersFavos =favoriteService.getMyFavOthersFavoByStudentId(student.getId());
           map.put("othersFavos",othersFavos);
           return ResponseEntity.ok(map);
        }
        map.put("success",false);
        return (ResponseEntity<Map<String, Object>>) ResponseEntity.notFound();
    }

}
