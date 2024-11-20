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

    public void saveNote(String title, String content, Long id) {
        Note note =new Note();
        note.setContent(content);
        note.setStudentId(id);
        note.setNoteTitle(title);
        noteRepository.save(note);
    }

    public boolean deleteNoteByNoteId(Long noteId) {
        noteRepository.deleteById(noteId);
        return !noteRepository.existsById(noteId);
    }
}
