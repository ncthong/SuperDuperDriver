package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("select * from CREDENTIALS where userid = #{userId}")
    List<Credential> getCredentialsByUserId(int userId);

    @Select("select * from CREDENTIALS where credentialid = #{credentialId} and userid = #{userId}")
    Credential getCredentialById(int credentialId,int userId);

    @Insert("insert into CREDENTIALS(url, username, key, password, userid) values (#{url}, #{username}, #{key}, #{password}, #{userId})")
    int insertCredential(Credential credential);

    @Update("update CREDENTIALS set url = #{url}, username = #{username}, key = #{key}, password = #{password} where credentialId = #{credentialId} and userid = #{userId}")
    int updateCredential(Credential credential);

    @Delete("delete from CREDENTIALS where credentialId = #{credentialId} and userid = #{userId}")
    int deleteCredential(int credentialId, int userId);
}
