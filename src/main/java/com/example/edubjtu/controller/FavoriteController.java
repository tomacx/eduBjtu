package com.example.edubjtu.controller;

import com.example.edubjtu.model.Favorite;
import com.example.edubjtu.model.Post;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.FavoriteService;
import com.example.edubjtu.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public String listFavorites(HttpSession session, Model model) {
        Long studentId = ((Student) session.getAttribute("loggedInStudent")).getId();
        List<Favorite> favorites = favoriteService.getFavoritesByStudentId(studentId);
        List<Post> favoritePosts = favorites.stream()
                .map(favorite -> postService.getPostById(favorite.getPostId()))
                .collect(Collectors.toList());
        model.addAttribute("favoritePosts", favoritePosts);
        return "favoriteList";
    }
}
