package com.example.edubjtu.controller;

import com.example.edubjtu.model.Note;
import com.example.edubjtu.model.Student;
import com.example.edubjtu.service.NoteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;



@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/list")
    public String listNotes(HttpSession session, Model model) {
        Long studentId = ((Student) session.getAttribute("loggedInStudent")).getId();
        List<Note> notes = noteService.getNotesByStudentId(studentId);
        model.addAttribute("notes", notes);
        return "noteList";
    }

    @GetMapping("/{id}")
    public String viewNote(@PathVariable Long id, Model model) {
        Note note = noteService.getNoteById(id);
        model.addAttribute("note", note);
        return "noteDetails";
    }


}
