package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
@AllArgsConstructor
public class SignupController {
    private final UserService userService;

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public String signupUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String errorMsg = "";

        if (userService.getUser(user.getUsername()) != null) {
            errorMsg = "The username already exists.";
        }
        if (errorMsg.isBlank()) {
            int cnt = userService.createUser(user);
            if (cnt == 0) {
                errorMsg = "There was an error signing you up. Please try again.";
            }
        }

        if (!errorMsg.isBlank()) {
            model.addAttribute("signupError", errorMsg);
            return "signup";
        }

        redirectAttributes.addFlashAttribute("signupSuccess", true);
        return "redirect:/login";
    }
}
