package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileUploadMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileUpload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class FileUploadService {

    public FileUploadMapper fileUploadMapper;

    public List<FileUpload> getFiles(Integer userId) {
        return fileUploadMapper.getFiles(userId);
    }
    public FileUpload getFileByName(MultipartFile fileUpload, Integer userId) {
        return fileUploadMapper.getFileByName(userId, handleFileName(fileUpload));
    }
    public FileUpload getFileById(Integer fileId, Integer userId) {
        return fileUploadMapper.getFileById(fileId, userId);
    }
    public int saveFile(MultipartFile fileUpload, Integer userId) throws IOException {
        FileUpload file = new FileUpload();
        file.setFileName(handleFileName(fileUpload));
        file.setUserId(userId);
        file.setContentType(fileUpload.getContentType());
        file.setFileData(fileUpload.getBytes());
        file.setFileSize(String.valueOf(fileUpload.getSize()));
        return fileUploadMapper.saveFile(file);
    }
    public int deleteFile(Integer fileId, Integer userId) {
        return fileUploadMapper.deleteFile(fileId, userId);
    }
    private String handleFileName(MultipartFile fileUpload){
        String fileName = fileUpload.getOriginalFilename();
        int index = fileName.replaceAll("\\\\","/").lastIndexOf("/");
        return fileName.substring(index+1);
    }
}
