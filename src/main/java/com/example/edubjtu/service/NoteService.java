package com.example.edubjtu.service;

import com.example.edubjtu.model.Note;
import com.example.edubjtu.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getNotesByStudentId(Long studentId) {
        return noteRepository.findByStudentId(studentId);
    }

    public Note getNoteById(Long noteId) {
        return noteRepository.findById(noteId).orElse(null);
    }
}
