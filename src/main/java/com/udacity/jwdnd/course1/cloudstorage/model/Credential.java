package com.udacity.jwdnd.course1.cloudstorage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credential {
    private Integer credentialId;
    private String url;
    private String username;
    @JsonIgnore
    private String key;
    private String password;
    private Integer userId;

    public Credential(String key, String url, String username, String password, Integer userId) {
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.userId = userId;
    }
}
