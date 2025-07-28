package com.example.filmpass.global.common;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;  // 응답 상태
    private String message; // 응답 메세지
    private Object  data;         // 실제 응답 데이터
    private LocalDateTime timestamp;  // 오류 상테 정보


    // 성공 응답 생성 (데어터 포함)
    public static ApiResponse success(Object data, String message) {
        return new ApiResponse(true, message, data, LocalDateTime.now());
    }

    //에러 응답 생성 (서버 내부 오류 등)
    public static  ApiResponse error(String message) {
        return new ApiResponse(false, message, null, LocalDateTime.now());
    }
}

