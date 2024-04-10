package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload {
    private String fileName;
    private Integer fileId;
    private String contentType;
    private String fileSize;
    private Integer userId;
    private byte[] fileData;
}
