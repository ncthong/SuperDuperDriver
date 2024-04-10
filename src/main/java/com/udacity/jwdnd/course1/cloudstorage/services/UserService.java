package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@AllArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public User getUserByName(String username){
        return userMapper.getUserByName(username);
    }
    public int addUser(User user){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodeSalt = Base64.getEncoder().encodeToString(salt);
        String hashPassword = hashService.getHashedValue(user.getPassword(), encodeSalt);
        return userMapper.insertUser(new User(null, user.getPassword(), encodeSalt, hashPassword, user.getFirstName(), user.getLastName()));
    }
}
