package com.example.filmpass.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordRequestDto {

    @Schema(description = "검증용 비밀번호", example = "Qwer1234!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;


    public PasswordRequestDto(String password) {
        this.password = password;
    }
}
