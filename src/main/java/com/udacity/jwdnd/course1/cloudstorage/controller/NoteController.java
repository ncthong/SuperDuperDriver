package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/delete")
    public String deleteNote(Authentication authentication, RedirectAttributes redirectAttributes, Integer noteId) {
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        int cnt = noteService.deleteNote(noteId,userId);
        if (cnt == 0) {
            redirectAttributes.addFlashAttribute("errorMsg", "Delete Note Failed. Try Again.");
            return "redirect:/result?error";
        }
        return "redirect:/result?success";
    }

    @PostMapping("/save")
    public String saveNote(Authentication authentication, RedirectAttributes redirectAttributes, Note note) {
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        Integer noteId = note.getNoteId();
        note.setUserId(userId);
        int cnt = 0;
        if(noteId != null){
            if (noteService.getNoteById(noteId, userId) != null) {
                cnt = noteService.updateNoteById(note);
            }
        }else {
            cnt = noteService.addNote(note);
        }
        if (cnt == 0) {
            redirectAttributes.addFlashAttribute("errorMsg", "Save Note Failed. Try Again.");
            return "redirect:/result?error";
        }
        return "redirect:/result?success";
    }
}
