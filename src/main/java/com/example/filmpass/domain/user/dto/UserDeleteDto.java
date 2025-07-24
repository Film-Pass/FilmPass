package com.example.filmpass.domain.user.dto;

import lombok.Getter;

@Getter
public class UserDeleteDto {

    private String password;


    public UserDeleteDto(String password) {
        this.password = password;
    }
}
