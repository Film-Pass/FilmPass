package com.example.filmpass.global.common.swagger.domain.auth;

import com.example.filmpass.domain.auth.dto.AuthData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpSuccess {

    @Schema(description = "요청 성공 여부", example = "true")
    private boolean success;

    @Schema(description = "응답 메시지", example = "회원가입 성공!")
    private String message;

    @Schema(description = "회원가입 응답 데이터")
    private AuthData data;

    @Schema(description = "응답 시간", example = "2025-08-08T12:34:56.789")
    private LocalDateTime timestamp;

}
