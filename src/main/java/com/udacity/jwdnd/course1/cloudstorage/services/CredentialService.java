package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public List<Credential> getAllCredentials(Integer userId) {
        return credentialMapper.getCredentialsByUserId(userId);
    }
    public Credential getCredentialById(Integer credentialId,Integer userId) {
        return credentialMapper.getCredentialById(credentialId,userId);
    }
    public void encryptPassword(Credential credential) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        String encodeKey = Base64.getEncoder().encodeToString(key);
        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), encodeKey);
        credential.setPassword(encryptPassword);
        credential.setKey(encodeKey);
    }
    public void decryptPassword(Credential credential) {
        if(credential.getKey() == null){
            return;
        }
        String decryptPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(decryptPassword);
    }

    public int addCredential(Credential credential) {
        encryptPassword(credential);
        return credentialMapper.insertCredential(credential);
    }
    public int updateCredential(Credential credential) {
        encryptPassword(credential);
        return credentialMapper.updateCredential(credential);
    }
    public int deleteCredential(Integer credentialId, Integer userId) {
        return credentialMapper.deleteCredential(credentialId, userId);
    }
}
