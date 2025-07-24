package com.example.filmpass.domain.user.dto;

import lombok.Getter;

@Getter
public class PasswordRequestDto {

    private String password;


    public PasswordRequestDto(String password) {
        this.password = password;
    }
}
