package com.example.filmpass.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthData {

    @Schema(description = "R - 사용자 이메일", example = "R - something123@domain.com")
    private final String email;

    @Schema(description = "R - 사용자 닉네임", example = "R - 별명")
    private final String nickname;

}
