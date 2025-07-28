package com.example.filmpass.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 극장 단건 조회로 추가
    THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, "극장을 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    // 상영관
    SCREEN_NOT_FOUND(HttpStatus.NOT_FOUND,"상영관을 찾을 수 없습니다" ),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "날짜를 찾을 수 없습니다."),

    // Auth
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 틀립니다."),
    CANNOT_CHANGE_SAME_ROLE(HttpStatus.BAD_REQUEST, "같은 권한으로 변경할 수 없습니다."),

    // JWT
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    WRONG_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DELETED_USER(HttpStatus.BAD_REQUEST, "이미 탈퇴한 유저입니다."),
    NOT_ADMIN(HttpStatus.BAD_REQUEST, "관리자 권한이 없습니다."),
    CHANGE_BLOCKED(HttpStatus.BAD_REQUEST, "본인의 정보만 수정할 수 있습니다."),

    THEATER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"극장이 이미 존재합니다."),

    // Movie
    MOVIE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"같은 이름의 영화가 이미 존재합니다"),
    MOVIE_LIST_NOT_FOUND(HttpStatus.NOT_FOUND,"영화가 존재하지 않습니다"),
    MOVIE_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND,"ID값에 해당하는 영화가 존재하지 않습니다."),
    MOVIE_NOT_FOUND_BY_TITLE(HttpStatus.NOT_FOUND,"제목과 일치하는 영화가 존재하지 않습니다."),
    MOVIE_NOT_FOUND_BY_DIRECTOR(HttpStatus.NOT_FOUND,"해당하는 작가의 영화가 존재하지 않습니다."),
    MOVIE_TITLE_REQUIRED(HttpStatus.BAD_REQUEST,"제목은 필수 항목입니다."),
    MOVIE_DIRECTOR_REQUIRED(HttpStatus.BAD_REQUEST,"작가는 필수 항목입니다."),
    MOVIE_RUNNING_TIME_REQUIRED(HttpStatus.BAD_REQUEST,"상영 시간은 필수 항목입니다.");



    private final HttpStatus code;
    private final String message;

    // 생성자
    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}