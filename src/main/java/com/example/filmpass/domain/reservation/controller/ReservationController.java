package com.example.filmpass.domain.reservation.controller;

import com.example.filmpass.domain.reservation.dto.*;
import com.example.filmpass.domain.reservation.service.ReservationService;
import com.example.filmpass.global.common.CommonResponse;
import com.example.filmpass.global.config.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "영화 예매", description = "영화티켓을 예매합니다")
    public ResponseEntity<CommonResponse> reserve(
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        ReservationResponse response = reservationService.reserve(userId, request);
        return ResponseEntity.ok(CommonResponse.success(response, "예매 완료"));
    }

    @DeleteMapping("/{reservationId}")
    @Operation(summary = "예매 취소", description = "예매를 취소합니다.")
    public ResponseEntity<CommonResponse> cancelReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getUserId();
        reservationService.cancelReservation(userId, reservationId);
        return ResponseEntity.ok(CommonResponse.success(null, "취소가 완료되었습니다."));
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "예매 정보 상세 조회", description = "예매 정보를 상세조회 합니다.")
    public ResponseEntity<CommonResponse> getReservationDetail(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long userId = userPrincipal.getUserId();
        ReservationDetailResponse reservationDetail = reservationService.getReservationDetail(userId, reservationId);
        return ResponseEntity.ok(CommonResponse.success(reservationDetail, "조회 성공"));
    }

    @GetMapping
    @Operation(summary = "예매목록 조회", description = "예매목록을 조회합니다.")
    public ResponseEntity<CommonResponse> getReservations(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = userPrincipal.getUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reservationAt"));
        Page<ReservationSummaryResponse> responses = reservationService.getReservationList(userId, pageable);
        return ResponseEntity.ok(CommonResponse.success(responses, "예매목록 조회 성공"));
    }

    // 결제 요금 계산하는 API (프론트에 전달하는 용도)
    @PostMapping("/calculation")
    @Operation(summary = "결제 요금 계산", description = "결제할 금액을 할인을 적용하여 계산합니다.")
    public ResponseEntity<CommonResponse> calculateAmounts(
            @RequestBody CalculateAmountRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal
            ) {

        return ResponseEntity.ok(
                CommonResponse.success(reservationService.calculateAmounts(requestDto, principal),
                        "결제 요금 계산 성공!"
                )
        );

    }

    // 결제 결과를 받고, 포인트 적립 API
    @PostMapping("/confirm")
    @Operation(summary = "결제 정보 수신 및 검증, 포인트 적립", description = "결제 토큰을 프론트에서 넘겨받고 결제 정보를 검증하는 요청을 보냅니다. 그리고 포인트를 적립합니다.")
    public ResponseEntity<CommonResponse> confirmPaymentAtBackend(
            @RequestBody PaymentConfirmRequestDtoFront requestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.ok(CommonResponse.success(
                null,
                reservationService.confirmPaymentAtBackend(requestDto, principal)));

    }

}
