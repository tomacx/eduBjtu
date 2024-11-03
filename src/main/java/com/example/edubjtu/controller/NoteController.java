package com.example.edubjtu.controller;

import com.example.edubjtu.model.Note;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.NoteService;
import com.example.edubjtu.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    StudentService studentService;

    @GetMapping("/getNote")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getNote(HttpSession session, @RequestParam String studentNum) {

        Student student = studentService.findStudentByStudentNum(studentNum);
        List<Note> noteList = noteService.getNotesByStudentId(student.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("notes", noteList);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/{id}")
    public String viewNote(@PathVariable Long id, Model model) {
        Note note = noteService.getNoteById(id);
        model.addAttribute("note", note);
        return "noteDetails";
    }


}
