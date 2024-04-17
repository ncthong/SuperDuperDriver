package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import lombok.AllArgsConstructor;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public List<File> getUserFiles(String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        return fileMapper.getFileByUser(user.getUserId());
    }

    public File getFileByNameId(Integer fileId, String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        // Validate File
        File oldFile = fileMapper.getFileById(fileId);
        if(oldFile == null || !Objects.equals(oldFile.getUserId(), user.getUserId())) {
            throw new InvalidArgumentException("Not Allowed!");
        }
        return oldFile;
    }

    public void addNewFile(MultipartFile file, String username) throws InvalidArgumentException, IOException {
        // Validate User
        File fileUpload = new File();
        User user = userMapper.getUser(username);
        String fileName = file.getOriginalFilename();
        File oldFile = fileMapper.getFileByNameId(fileName, user.getUserId());
        if (file.isEmpty()) {
            throw new InvalidArgumentException("File is not chosen or empty");
        }
        // Validate Unique File Name
        if(oldFile != null) {
            throw new InvalidArgumentException("File with same name already exists!");
        }

        fileUpload.setFileName(fileName);
        fileUpload.setContentType(file.getContentType());
        fileUpload.setFileSize(String.valueOf(file.getSize()));
        fileUpload.setUserId(user.getUserId());
        fileUpload.setFileData(file.getBytes());
        fileMapper.insert(fileUpload);
    }

    public void deleteUserFile(Integer fileId, String username) throws InvalidArgumentException, IOException {
        // Validate User
        User user = userMapper.getUser(username);
        File files = fileMapper.getFileById(fileId);
        if(files == null || !Objects.equals(files.getUserId(), user.getUserId())) {
            throw new InvalidArgumentException("Not exist file.");
        }
        fileMapper.delete(files.getFileId());
    }
}
