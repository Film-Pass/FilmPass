package com.example.filmpass.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;


    public PasswordRequestDto(String password) {
        this.password = password;
    }
}
