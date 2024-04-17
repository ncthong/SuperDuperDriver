package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/credential")
@AllArgsConstructor
public class CredentialController {
    private final CredentialService credentialService;

    @GetMapping
    public String getUserCredentials(Authentication authentication, Model model, RedirectAttributes redirectAttributes){
        try {
            String username = authentication.getName();
            model.addAttribute("credentials", this.credentialService.getUserCredentials(username));
            return "home";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

    @GetMapping( "/decrypt-password/{credentialId}")
    @ResponseBody
    public Map<String, String> decryptCredential(Authentication authentication, @PathVariable Integer credentialId) {
        String username = authentication.getName();
        String rawPassword = credentialService.decryptPassword(credentialId, username);
        Map<String, String> response = new HashMap<>();
        response.put("decryptedPassword", rawPassword);
        return response;
    }

    @PostMapping
    public String addUserCredential(Authentication authentication,Credential credential,RedirectAttributes redirectAttributes,Model model) {
        try {
            String username = authentication.getName();
            int cnt = this.credentialService.addNewCredential(credential, username);
            if(cnt == 0){
                throw new Exception("Save Credential failed. Try again!");
            }
            List<Credential> credentials = this.credentialService.getUserCredentials(username);
            model.addAttribute("credentials", credentials);
            return "redirect:/result?success";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteUserCredential(Authentication authentication,@PathVariable Integer credentialId,RedirectAttributes redirectAttributes,Model model) {
        try {
            String username = authentication.getName();
            this.credentialService.deleteCredential(credentialId);
            model.addAttribute("credentials", this.credentialService.getUserCredentials(username));
            return "redirect:/result?success";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }
}
