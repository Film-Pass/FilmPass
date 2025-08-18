package com.example.filmpass.domain.seat.controller;

import com.example.filmpass.domain.seat.dto.SeatRequest;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.service.SeatService;
import com.example.filmpass.global.aop.TrackUserActionAnnotation;
import com.example.filmpass.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
@Validated
public class SeatController {

    private final SeatService seatService;

    // 좌석 등록 (상영관 연관관계, 어도민 권한)
    @PostMapping
    @Operation(summary = "좌석 등록", description = "상영관내 좌석을 등록합니다.")
    @TrackUserActionAnnotation("좌석 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse> createSeats(@RequestBody @Valid List<SeatRequest> requests) {
        List<SeatResponse> responses = seatService.createSeats(requests);
        return ResponseEntity.ok(CommonResponse.success(responses, "좌석 일괄 등록 완료"));
    }

    // 좌석 목록 조회
    @GetMapping
    @Operation(summary = "좌석 목록 조회", description = "상영관내 좌석 목록을 조회합니다.")
    public ResponseEntity<CommonResponse> getAllSeats() {
        List<SeatResponse> responses = seatService.getAllSeats();
        return ResponseEntity.ok(CommonResponse.success(responses, "전체 좌석 목록 조회 성공"));
    }

    // 좌석 단건 조회
    @GetMapping("/{seatId}")
    @Operation(summary = "좌석 정보 조회", description = "좌석 하나의 정보를 조회합니다.")
    public ResponseEntity<CommonResponse> getSeatById(@PathVariable Long seatId) {
        SeatResponse seat = seatService.getSeatById(seatId);
        return ResponseEntity.ok(CommonResponse.success(seat, "좌석 단건 조회 성공"));
    }

    // 좌석 수정 (어도민 권한)
    @PatchMapping("/{seatId}")
    @Operation(summary = "(관리자 전용) 좌석 정보 수정", description = "좌석의 정보를 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @TrackUserActionAnnotation("좌석 수정")
        public ResponseEntity<CommonResponse> updateSeat(@PathVariable Long seatId, @RequestBody SeatRequest request) {
        SeatResponse updatedSeat = seatService.updateSeat(seatId, request);
        return ResponseEntity.ok(CommonResponse.success(updatedSeat, "좌석 수정 성공"));
    }

    // 고장난 좌석으로 변경
    @PatchMapping("/{seatId}/broken")
    @Operation(summary = "(관리자 전용) 고장난 좌석으로 변경", description = "좌석을 고장난 좌석으로 변경합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse> markSeatAsBroken(@PathVariable Long seatId) {
        seatService.markAsBroken(seatId);
        return ResponseEntity.ok(CommonResponse.success(null, "고장난 좌석으로 변경 완료"));
    }

    // 고장 복구 (사용 가능 상태로 변경)
    @PatchMapping("/{seatId}/available")
    @Operation(summary = "(관리자 전용) 고장 복구", description = "고장난 좌석을 정상적인 좌석으로 되돌립니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse> markSeatAsAvailable(@PathVariable Long seatId) {
        seatService.markAsAvailable(seatId);
        return ResponseEntity.ok(CommonResponse.success(null, "좌석 상태를 사용 가능으로 변경 완료"));
    }
}