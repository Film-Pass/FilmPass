package com.example.filmpass.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseObject {
    private boolean success;  // 응답 상태
    private String message; // 응답 메세지
    private Object data;         // 실제 응답 데이터
    private LocalDateTime timestamp;  // 오류 상테 정보

    // 성공 응답 생성 (데어터 포함)
    public static ApiResponseObject success(Object data, String message) {
        return new ApiResponseObject(true, message, data, LocalDateTime.now());
    }

    //에러 응답 생성 (서버 내부 오류 등)
    public static ApiResponseObject error(String message) {
        return new ApiResponseObject(false, message, null, LocalDateTime.now());
    }
}

