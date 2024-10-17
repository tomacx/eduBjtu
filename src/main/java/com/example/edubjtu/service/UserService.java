package com.example.edubjtu.service;

import com.example.edubjtu.entity.User;
import com.example.edubjtu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
