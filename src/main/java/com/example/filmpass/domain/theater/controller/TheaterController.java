package com.example.filmpass.domain.theater.controller;

import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.service.TheaterService;
import com.example.filmpass.global.aop.TrackUserActionAnnotation;
import com.example.filmpass.global.common.CommonResponse;
import com.example.filmpass.domain.theater.dto.PagedResponse;
import com.example.filmpass.domain.theater.dto.TheaterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    @Operation(summary = "(관리자 전용) 극장 생성", description = "극장을 생성합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @TrackUserActionAnnotation("극장 등록")
    public ResponseEntity<CommonResponse> createTheater(@Valid @RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.createTheater(request);
        return ResponseEntity.ok(CommonResponse.success(response, "극장 등록 성공"));
    }

    // 극장 단건 조회
    @GetMapping("/{id}")
    @Operation(summary = "극장 정보 조회", description = "극장의 정보를 조회합니다.")
    public ResponseEntity<CommonResponse> getTheaterById(@PathVariable Long id) {
        TheaterResponse response = theaterService.getTheaterById(id);
        return ResponseEntity.ok(CommonResponse.success(response, "극장 단건 조회 성공"));
    }

    // 극장 목록 조회
    @GetMapping
    @Operation(summary = "극장의 목록을 조회합니다.", description = "극장목록과 극장정보를 조회합니다.")
    public ResponseEntity<CommonResponse> getAllTheaters(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        PagedResponse<TheaterResponse> theaters = theaterService.getAllTheaters(pageable);
        return ResponseEntity.ok(CommonResponse.success(theaters, "극장 목록 조회 성공"));
    }

    // 극장 수정
    @PatchMapping("/{id}")
    @Operation(summary = "(관리자 전용) 극장 정보 수정.", description = "극장의 정보를 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @TrackUserActionAnnotation("극장 수정")
    public ResponseEntity<CommonResponse> updateTheater(
            @PathVariable Long id,
            @RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.updateTheater(id, request);
        return ResponseEntity.ok(CommonResponse.success(response, "극장 수정 성공"));
    }
}