package com.example.edubjtu.repository;

import com.example.edubjtu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,String>{
    User findByUsername(String username);
}
