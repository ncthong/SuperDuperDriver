package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileUpload;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/fileUpload")
public class FileUploadController {
    private FileUploadService fileUploadService;

    @GetMapping("/view")
    public ResponseEntity<byte[]> handleViewFile(Integer fileId, Authentication authentication){
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        FileUpload file = fileUploadService.getFileById(fileId, userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getFileName());
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        return new ResponseEntity<>(file.getFileData(),headers, HttpStatus.OK);
    }
    @PostMapping("/upload")
    public String handleUploadFile(MultipartFile fileUpload, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException{
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        String errorMsg = "";

        if (fileUpload.isEmpty()) {
            errorMsg = "File is not chosen or empty";
        }
        FileUpload fileExist = fileUploadService.getFileByName(fileUpload,userId);
        if (fileExist != null) {
            errorMsg = "File already exists";
        }
        if (!errorMsg.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/result?error";
        }
        int cnt = fileUploadService.saveFile(fileUpload, userId);
        if(cnt == 0){
            redirectAttributes.addFlashAttribute("errorMsg", "An error occurred during the file upload process");
            return "redirect:/result?error";
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String handleDeleteFile(Integer fileId, Authentication authentication, RedirectAttributes redirectAttributes){
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        int cnt = fileUploadService.deleteFile(fileId,userId);
        if(cnt == 0){
            redirectAttributes.addFlashAttribute("errorMsg", "Not exist file.");
            return "redirect:/result?error";
        }
        return "redirect:/result?success";
    }
}
