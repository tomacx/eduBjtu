package com.example.edubjtu.controller;

import com.example.edubjtu.entity.User;
import com.example.edubjtu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model){
        User nowUser = userService.findUserByUsername(user.getUsername());
        if(nowUser != null && nowUser.getPassword().equals(user.getPassword())){
            return "redirect:/welcome";
        }
        model.addAttribute("error", "用户名或密码错误");
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }
}
