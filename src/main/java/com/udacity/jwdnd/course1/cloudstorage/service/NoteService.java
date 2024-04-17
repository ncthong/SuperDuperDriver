package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import lombok.AllArgsConstructor;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public List<Note> getUserNotes(String username) throws InvalidArgumentException {
        User user = userMapper.getUser(username);
        return noteMapper.getUserNotes(user.getUserId());
    }

    public void addNewNote(Note note, String username) throws InvalidArgumentException {
        User user = userMapper.getUser(username);
        // Add Note
        if(note.getNoteId() == null) {
            Note newNote = new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), user.getUserId());
            noteMapper.insert(newNote);
        }
        // Update Note
        else {
            // Verify Note belongs to user
            Note userNote = noteMapper.getNote(note.getNoteId());
            if(!Objects.equals(userNote.getUserId(), user.getUserId())){
                throw new InvalidArgumentException("User action not allowed!");
            }
            // Update Title & Description
            userNote.setNoteTitle(note.getNoteTitle());
            userNote.setNoteDescription(note.getNoteDescription());
            noteMapper.update(userNote);
        }
    }

    public void deleteNote(Integer noteId) throws InvalidArgumentException {
        int cnt = noteMapper.delete(noteId);
        if(cnt == 0) {
            throw new InvalidArgumentException("Delete Note failed. Try again!");
        }
    }
}
