package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class SignupController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(User user, Model model, RedirectAttributes redirectAttributes) {
        String err = "";

        if (userService.getUserByName(user.getUsername()) != null) {
            err = "The username already exists.";
        }

        if (err.isBlank()) {
            int cnt = userService.addUser(user);
            if (cnt == 0) {
                err = "There was an error signing you up. Please try again.";
            }
        }

        if (!err.isBlank()) {
            model.addAttribute("signupError", err);
            return "signup";
        }
        redirectAttributes.addFlashAttribute("signupSuccess", true);
        return "redirect:/login";
    }
}
