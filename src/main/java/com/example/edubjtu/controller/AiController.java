package com.example.edubjtu.controller;

import com.example.edubjtu.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ai")
public class AiController {
    @Autowired
    private AiService aiService;
    @GetMapping("/courseAi")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> courseAi(@RequestParam String question) throws IOException {
        String answer = AiService.getAnswerOfAi(question);
        Map<String, Object> map = new HashMap<>();
        map.put("answer", answer);
        return ResponseEntity.ok(map);
    }

}
