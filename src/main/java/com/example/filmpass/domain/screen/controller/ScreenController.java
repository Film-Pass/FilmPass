package com.example.filmpass.domain.screen.controller;

import com.example.filmpass.domain.screen.dto.ScreenRequestDto;
import com.example.filmpass.domain.screen.dto.ScreenResponseDto;
import com.example.filmpass.domain.screen.service.ScreenService;
import com.example.filmpass.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    // 상영관 생성
    @PostMapping
    public ResponseEntity<ApiResponse> createScreen(@Valid @RequestBody ScreenRequestDto requestDto) {
        ScreenResponseDto response = screenService.createScreen(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "상영관이 등록되었습니다."));
    }
    // 상영관 수정
    @PatchMapping("/{screenId}")
    public ResponseEntity<ApiResponse> updateScreen(@PathVariable Long screenId, @Valid @RequestBody ScreenRequestDto requestDto) {
        ScreenResponseDto response = screenService.updateScreen(screenId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(response, "상영관이 수정되었습니다."));
    }
}