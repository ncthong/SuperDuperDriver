package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import lombok.AllArgsConstructor;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping
    public String getUserFiles(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            model.addAttribute("files", this.fileService.getUserFiles(username));
            return "home";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

    @PostMapping
    public String uploadUserFile(Authentication authentication,MultipartFile fileUpload,RedirectAttributes redirectAttributes,Model model) {
        String username = authentication.getName();
        String errorMsg = "";
        try {
            fileService.addNewFile(fileUpload, username);
            model.addAttribute("files", this.fileService.getUserFiles(username));
            return "redirect:/result?success";
        } catch (Exception e) {
            errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

    @GetMapping( "/view/{fileId}")
    public ResponseEntity<byte[]> viewUserFile(Authentication authentication,@PathVariable Integer fileId){
        String username = authentication.getName();
        try {
            File file = fileService.getFileByNameId(fileId, username);
            String fileName = file.getFileName();
            String contentType = file.getContentType();
            byte[] fileData = file.getFileData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.parseMediaType(contentType));
            return new ResponseEntity<>(fileData,headers, HttpStatus.OK);

        } catch (InvalidArgumentException e) {
            return ResponseEntity.internalServerError().body("File not found".getBytes());
        }
    }

    @GetMapping( "/delete/{fileId}")
    public String deleteUserFile(Authentication authentication,@PathVariable Integer fileId,RedirectAttributes redirectAttributes,Model model){
        String username = authentication.getName();
        try{
            fileService.deleteUserFile(fileId, username);
            model.addAttribute("files", this.fileService.getUserFiles(username));
            return "redirect:/result?success";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
    }

}
