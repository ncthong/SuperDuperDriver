package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.FileUpload;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileUploadMapper {

    @Select("select * from FILES where userid = #{userId}")
    List<FileUpload> getFiles(Integer userId);

    @Select("select * from FILES where userid = #{userId} and filename = #{fileName}")
    FileUpload getFileByName(Integer userId, String fileName);

    @Select("select * from FILES where fileid = #{fileId} and userid = #{userId}")
    FileUpload getFileById(Integer fileId, Integer userId);

    @Insert("insert into FILES (filename, contenttype, filesize, userid, filedata) values (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    int saveFile(FileUpload file);

    @Delete("delete from FILES where fileid = #{fileId} and userid = #{userId}")
    int deleteFile(Integer fileId, Integer userId);
}
