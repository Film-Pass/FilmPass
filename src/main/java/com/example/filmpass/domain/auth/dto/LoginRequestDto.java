package com.example.filmpass.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String email;
    private String password;


    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
