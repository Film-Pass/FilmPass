package com.example.filmpass.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    // 극장 단건 조회로 추가
    THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, "극장을 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    // Auth
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    THEATER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"극장이 이미 존재합니다.");

    private final HttpStatus code;
    private final String message;

    // 생성자
    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}