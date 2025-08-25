package com.example.filmpass.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //시스템
    THREAD_INTERRUPTED(HttpStatus.CONFLICT, "쓰레드가 인터럽트되었습니다."),
    // 극장 단건 조회로 추가
    THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, "극장을 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다."),

    // 리뷰
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    CRITIC_ONLY(HttpStatus.FORBIDDEN, "평론가만 작성할 수 있는 리뷰입니다."),
    SCHEDULE_OVERLAP(HttpStatus.CONFLICT,"이미 존재하는 일정과 시간이 겹칩니다."),

    // 상영관
    SCREEN_NOT_FOUND(HttpStatus.NOT_FOUND,"상영관을 찾을 수 없습니다" ),
    SCREEN_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 이름의 상영관이 이미 존재합니다."),

    // 스케쥴
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "날짜를 찾을 수 없습니다."),
    SCHEDULE_TIME_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 일정과 시간이 겹칩니다."),

    // 좌석
    DUPLICATE_SEAT_NUMBER(HttpStatus.BAD_REQUEST, "이미 등록된 좌석번호입니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "좌석을 찾을 수 없습니다."),
    SEAT_RESERVATION_LOCKED(HttpStatus.CONFLICT, "누군가 예매 중인 좌석 입니다."),
    BROKEN_SEAT(HttpStatus.BAD_REQUEST, "고장난 좌석은 예매할 수 없습니다."),
    // Auth
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 틀립니다."),
    CANNOT_CHANGE_SAME_ROLE(HttpStatus.BAD_REQUEST, "같은 권한으로 변경할 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Refresh Token 이 존재하지 않습니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCESS Token 이 존재하지 않습니다."),
    // JWT
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    WRONG_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 입니다."),
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DELETED_USER(HttpStatus.BAD_REQUEST, "이미 탈퇴한 유저입니다."),
    NOT_ADMIN(HttpStatus.BAD_REQUEST, "관리자 권한이 없습니다."),
    CHANGE_BLOCKED(HttpStatus.BAD_REQUEST, "본인의 정보만 수정할 수 있습니다."),
    THEATER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"극장이 이미 존재합니다."),
    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "유효하지 않은 페이지 크기 입니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "유효하지 않는 페이지 번호 입니다."),
    EMPTY_PAGE(HttpStatus.NOT_FOUND, "비어있는 페이지 입니다."),
    // Payment
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 order_id 입니다."),
    PAYMENTKEY_MISMATCH(HttpStatus.BAD_REQUEST, "결제된 키와 검증요청한 키가 서로 다릅니다."),
    AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "결제된 금액과 검증요청한 금액이 서로 다릅니다."),
    PAYMENT_FAILED(HttpStatus.UNAUTHORIZED, "결제 실패!"),
    // Reservation
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예매 정보를 찾을 수 없습니다."),
    ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 예매입니다."),
    NOT_YOUR_RESERVATION(HttpStatus.FORBIDDEN, "예매 정보에 접근할 수 없습니다."),
    SEAT_ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "이미 예약된 좌석이 포함되어 있습니다."),
    INVALID_SCHEDULE(HttpStatus.BAD_REQUEST, "상영 회차가 존재하지 않습니다."),
    // Movie
    MOVIE_LIST_NOT_FOUND(HttpStatus.NOT_FOUND,"영화가 존재하지 않습니다"),
    MOVIE_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND,"ID값에 해당하는 영화가 존재하지 않습니다."),
    MOVIE_NOT_FOUND_BY_TITLE(HttpStatus.NOT_FOUND,"제목과 일치하는 영화가 존재하지 않습니다."),
    MOVIE_NOT_FOUND_BY_DIRECTOR(HttpStatus.NOT_FOUND,"해당하는 감독의 영화가 존재하지 않습니다."),
    MOVIE_TITLE_REQUIRED(HttpStatus.BAD_REQUEST,"제목은 필수 항목입니다."),
    MOVIE_DIRECTOR_REQUIRED(HttpStatus.BAD_REQUEST,"감독은 필수 항목입니다."),
    MOVIE_GENRE_REQUIRED(HttpStatus.BAD_REQUEST, "장르는 필수 항목입니다."),
    MOVIE_RUNNING_TIME_REQUIRED(HttpStatus.BAD_REQUEST,"상영 시간은 필수 항목입니다."),
    MOVIE_SEARCH_REQUIRED(HttpStatus.BAD_REQUEST, "ID, 제목, 감독, 장르 중 최소 1가지 이상의 조건이 필요합니다."),
    MOVIE_SEARCH_NOT_FOUND(HttpStatus.NOT_FOUND,"조건에 일치하는 영화가 존재하지 않습니다."),
    MOVIE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 영화입니다.");

    private final HttpStatus code;
    private final String message;

    // 생성자
    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}