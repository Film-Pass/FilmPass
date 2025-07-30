package com.example.filmpass.domain.screen.contorller;

import com.example.filmpass.domain.screen.dto.ScreenRequestDto;
import com.example.filmpass.domain.screen.service.ScreenService;
import com.example.filmpass.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/screens")
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ApiResponse> createScreen(
            @RequestBody @Valid ScreenRequestDto requestDto
    ) {
        screenService.createScreen(requestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "상영관이 등록되었습니다."));
    }
}
