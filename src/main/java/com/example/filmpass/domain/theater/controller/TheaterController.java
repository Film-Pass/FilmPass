package com.example.filmpass.domain.theater.controller;

import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.service.TheaterService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.domain.theater.dto.PagedResponse;
import com.example.filmpass.domain.theater.dto.TheaterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    // 극장 등록 (어드민 권한)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createTheater(@RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.createTheater(request);
        return ResponseEntity.ok(ApiResponse.success(response, "좌석 등록 성공"));
    }

    // 극장 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTheaterById(@PathVariable Long id) {
        TheaterResponse response = theaterService.getTheaterById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "극장 단건 조회 성공"));
    }

    // 극장 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse> getAllTheaters(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        PagedResponse<TheaterResponse> theaters = theaterService.getAllTheaters(pageable);
        return ResponseEntity.ok(ApiResponse.success(theaters, "극장 목록 조회 성공"));
    }

    // 극장 수정
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateTheater(
            @PathVariable Long id,
            @RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.updateTheater(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "극장 수정 성공"));
    }
}
