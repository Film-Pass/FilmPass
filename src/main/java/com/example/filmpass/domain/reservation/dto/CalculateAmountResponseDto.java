package com.example.filmpass.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculateAmountResponseDto {

    private int originalAmount;     // 할인 전
    private int discountAmount;     // 할인된 금액
    private int finalAmount;        // 최종 결제 금액
    private String discountReason;  // 할인 사유
    private String orderId;         // 주문 식별자

}
