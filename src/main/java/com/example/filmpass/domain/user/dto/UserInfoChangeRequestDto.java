package com.example.filmpass.domain.user.dto;

import lombok.Getter;

@Getter
public class UserInfoChangeRequestDto {

    private String name;
    private String email;
    private String password;
    private String nickname;


    public UserInfoChangeRequestDto(String name, String email, String password, String nickname) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
