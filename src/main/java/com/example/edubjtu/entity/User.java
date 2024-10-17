package com.example.edubjtu.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
@Entity
@Table(name = "user")
public class User {
   @Id
   private long id;
   private String username;
   private String userNum;
   private String password;

   //get
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public void setUsername(String username){
        this.username = username;
    }
}