package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Credential {
    private Integer credentialId;
    private String url;
    private String username;
    private String keys;
    private String password;
    private Integer userId;

}
