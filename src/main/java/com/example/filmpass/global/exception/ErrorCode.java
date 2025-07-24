package com.example.filmpass.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),

    // Auth
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 틀립니다."),

    // JWT
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    WRONG_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DELETED_USER(HttpStatus.BAD_REQUEST, "이미 탈퇴한 유저입니다.");

    private final HttpStatus code;
    private final String message;

    // 생성자
    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}