package com.example.filmpass.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    // 극장 수정으로 이름 수정
    THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, "극장을 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다.");

    private final HttpStatus code;
    private final String message;

    // 생성자
    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}