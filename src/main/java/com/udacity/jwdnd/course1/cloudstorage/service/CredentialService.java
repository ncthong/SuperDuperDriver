package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import lombok.AllArgsConstructor;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final HashService hashService;
    private final EncryptionService encryptionService;

    public List<Credential> getUserCredentials(String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        if(user == null) {
            throw new InvalidArgumentException("User not found!");
        }
        return credentialMapper.getUserCredentials(user.getUserId());
    }

    public int addNewCredential(Credential credential, String username) throws InvalidArgumentException {
        User user = userMapper.getUser(username);
        // New Credential
        if(credential.getCredentialId() == null) {
            encryptPassword(credential);
            Credential newCredential = new Credential(
                    credential.getCredentialId(),
                    credential.getUrl(),
                    credential.getUsername(),
                    credential.getKeys(),
                    credential.getPassword(),
                    user.getUserId()
            );
            return credentialMapper.insert(newCredential);
        }
        // Update Credential
        else {
            Credential oldCredential = credentialMapper.getCredential(credential.getCredentialId());
            oldCredential.setUrl(credential.getUrl());
            oldCredential.setUsername(credential.getUsername());
            String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), oldCredential.getKeys());
            oldCredential.setPassword(encryptedPassword);
            return credentialMapper.update(oldCredential);
        }
    }



    public void deleteCredential(Integer credentialId) throws InvalidArgumentException {
        int cnt = credentialMapper.delete(credentialId);
        if (cnt == 0){
            throw new InvalidArgumentException("Delete Credential failed. Try again!");
        }
    }
    public void encryptPassword(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKeys(encodedKey);
    }
    public String decryptPassword(Integer credentialId, String username) throws InvalidArgumentException {
        // Validate User
        User user = userMapper.getUser(username);
        Credential credential = credentialMapper.getCredential(credentialId);
        if(credential == null) {
            throw new InvalidArgumentException("Credential Not Found");
        }
        // Validate Credential User
        if(!Objects.equals(credential.getUserId(), user.getUserId())) {
            throw new InvalidArgumentException("Invalid User Access");
        }
        return encryptionService.decryptValue(credential.getPassword(), credential.getKeys());
    }
}
