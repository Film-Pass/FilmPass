package com.example.filmpass.domain.reservation.controller;

import com.example.filmpass.domain.reservation.dto.ReservationDetailResponse;
import com.example.filmpass.domain.reservation.dto.ReservationRequest;
import com.example.filmpass.domain.reservation.dto.ReservationResponse;
import com.example.filmpass.domain.reservation.dto.ReservationSummaryResponse;
import com.example.filmpass.domain.reservation.service.ReservationService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> reserve(
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        ReservationResponse response = reservationService.reserve(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "예매 완료"));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getUserId();
        reservationService.cancelReservation(userId, reservationId);
        return ResponseEntity.ok(ApiResponse.success(null, "취소가 완료되었습니다."));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDetailResponse>> getReservationDetail(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getUserId();
        ReservationDetailResponse reservationDetail = reservationService.getReservationDetail(userId, reservationId);
        return ResponseEntity.ok(ApiResponse.success(reservationDetail, "조회 성공"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReservationSummaryResponse>>> getReservations(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = userPrincipal.getUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reservationAt"));
        Page<ReservationSummaryResponse> responses = reservationService.getReservationList(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(responses, "예매목록 조회 성공"));
    }
}
