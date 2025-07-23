package com.example.filmpass.domain.theater.controller;

import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.service.TheaterService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    // 극장 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<TheaterResponse>>> getAllTheaters() {
        List<TheaterResponse> theaters = theaterService.getAllTheaters();
        return ResponseEntity.ok(
                ApiResponse.success(theaters, "극장 목록 조회 성공")
        );
    }
}
