package com.example.filmpass.domain.payment;

import com.example.filmpass.domain.payment.dto.PaymentConfirmRequestDto;
import com.example.filmpass.domain.payment.dto.PaymentCalculationRequestDtoFront;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    // 결제 진행 API
    @PostMapping("/api/payments/calculation")
    public ResponseEntity<ApiResponse> createPaymentKey(
            @Valid @RequestBody PaymentCalculationRequestDtoFront requestDto,
            @AuthenticationPrincipal UserPrincipal principal // Payment 전용 엔티티를 하나 생성해야 하기에 유저정보 재활용함.
            ) {

        return ResponseEntity.ok(
                ApiResponse.success(paymentService.createPaymentKey(requestDto, principal),
                        "결제 진행 및 토큰 생성 완료!"));

    }

    // 결제 검증 API
    @PostMapping("/api/payments/confirm")
    public ResponseEntity<ApiResponse> confirmPaymentAtPG(@RequestBody PaymentConfirmRequestDto requestDto) {

        return ResponseEntity.ok(ApiResponse.success(
                paymentService.confirmPaymentAtPG(requestDto), "결제 검증 성공!"
                )
        );

    }

}
