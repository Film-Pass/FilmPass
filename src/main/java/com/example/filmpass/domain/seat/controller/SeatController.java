package com.example.filmpass.domain.seat.controller;

import com.example.filmpass.domain.seat.dto.SeatRequest;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.service.SeatService;
import com.example.filmpass.global.aop.TrackUserActionAnnotation;
import com.example.filmpass.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    // 좌석 등록 (상영관 연관관계, 어도민 권한)
    @PostMapping
    @TrackUserActionAnnotation("좌석 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createSeats(@Valid @RequestBody List<SeatRequest> requests) {
        List<SeatResponse> responses = seatService.createSeats(requests);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responses, "좌석 일괄 등록 완료"));
    }

    // 좌석 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse> getAllSeats() {
        List<SeatResponse> responses = seatService.getAllSeats();
        return ResponseEntity.ok(ApiResponse.success(responses, "전체 좌석 목록 조회 성공"));
    }

    // 좌석 단건 조회
    @GetMapping("/{seatId}")
    public ResponseEntity<ApiResponse> getSeatById(@PathVariable Long seatId) {
        SeatResponse seat = seatService.getSeatById(seatId);
        return ResponseEntity.ok(ApiResponse.success(seat, "좌석 단건 조회 성공"));
    }

    // 좌석 수정 (어도민 권한)
    @PatchMapping("/{seatId}")
    @PreAuthorize("hasRole('ADMIN')")
    @TrackUserActionAnnotation("좌석 수정")
        public ResponseEntity<ApiResponse> updateSeat(
                @PathVariable Long seatId,
                @RequestBody SeatRequest request) {

        SeatResponse updatedSeat = seatService.updateSeat(seatId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedSeat, "좌석 수정 성공"));
    }

    // 고장난 좌석으로 변경
    @PatchMapping("/{seatId}/broken")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> markSeatAsBroken(@PathVariable Long seatId) {
        seatService.markAsBroken(seatId);
        return ResponseEntity.ok(ApiResponse.success(null, "고장난 좌석으로 변경 완료"));
    }

    // 고장 복구 (사용 가능 상태로 변경)
    @PatchMapping("/{seatId}/available")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> markSeatAsAvailable(@PathVariable Long seatId) {
        seatService.markAsAvailable(seatId);
        return ResponseEntity.ok(ApiResponse.success(null, "좌석 상태를 사용 가능으로 변경 완료"));
    }
}