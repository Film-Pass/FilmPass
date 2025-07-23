package com.example.filmpass.domain.theater.controller;

import com.example.filmpass.domain.theater.dto.TheaterRequest;
import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.service.TheaterService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theaters")
public class TheaterController {

    private final TheaterService theaterService;

   // 극장 수정
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 수정 가능
    public ResponseEntity<ApiResponse<TheaterResponse>> updateTheater(
            @PathVariable Long id,
            @RequestBody TheaterRequest request
    ) {
        TheaterResponse response = theaterService.updateTheater(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "극장 수정 성공"));
    }
}
