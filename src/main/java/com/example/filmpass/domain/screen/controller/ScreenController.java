package com.example.filmpass.domain.screen.controller;

import com.example.filmpass.domain.screen.dto.ScreenRequestDto;
import com.example.filmpass.domain.screen.dto.ScreenResponseDto;
import com.example.filmpass.domain.screen.service.ScreenService;
import com.example.filmpass.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "상영관 생성", description = "극장내 상영관을 생성합니다.")
    public ResponseEntity<CommonResponse> createScreen(@Valid @RequestBody ScreenRequestDto requestDto) {
        ScreenResponseDto response = screenService.createScreen(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(response, "상영관이 등록되었습니다."));
    }
    // 상영관 수정
    @PatchMapping("/{screenId}")
    @Operation(summary = "상영관 정보 수정", description = "상영관의 정보를 수정합니다.")
    public ResponseEntity<CommonResponse> updateScreen(@PathVariable Long screenId, @Valid @RequestBody ScreenRequestDto requestDto) {
        ScreenResponseDto response = screenService.updateScreen(screenId, requestDto);
        return ResponseEntity.ok(CommonResponse.success(response, "상영관이 수정되었습니다."));
    }
}