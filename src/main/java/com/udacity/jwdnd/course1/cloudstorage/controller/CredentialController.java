package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/credential")
public class CredentialController {
    private final CredentialService credentialService;

    @GetMapping("/view")
    @ResponseBody
    public Credential getCredential(Integer credentialId, Authentication authentication){
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        Credential credential = credentialService.getCredentialById(credentialId,userId);
        if(credential == null){
            return null;
        }
        credentialService.decryptPassword(credential);
        return credential;
    }

    @PostMapping("/save")
    public String handleSaveCredential(Credential credential, Authentication authentication, RedirectAttributes redirectAttributes){
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        Integer credentialId = credential.getCredentialId();
        credential.setUserId(userId);
        int cnt = 0;
        if(credentialId != null){
            if(credentialService.getCredentialById(credentialId, userId) != null){
                cnt = credentialService.updateCredential(credential);
            }
        }else {
            cnt = credentialService.addCredential(credential);
        }
        if(cnt == 0){
            redirectAttributes.addFlashAttribute("errorMsg", "Credential not saved. Try Again.");
            return "redirect:/result?error";
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String handleDeleteCredential(Integer credentialId, Authentication authentication, RedirectAttributes redirectAttributes){
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        int cnt = credentialService.deleteCredential(credentialId,userId);
        if(cnt == 0){
            redirectAttributes.addFlashAttribute("errorMsg", "Credential not deleted. Try Again.");
            return "redirect:/result?error";
        }
        return "redirect:/result?success";
    }
}
