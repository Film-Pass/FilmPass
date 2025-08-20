package com.example.filmpass.global.exception;


import com.example.filmpass.global.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getCode())
                .body(CommonResponse.error(e.getErrorCode().getMessage()));
    }

    // Enum 에 없는 UserRole 입력시 발생하는 예외
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse> handleEnumParseError(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.error("변경하려는 권한값이 존재하지 않습니다."));
    }

}
