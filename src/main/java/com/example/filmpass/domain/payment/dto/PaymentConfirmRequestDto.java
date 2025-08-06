package com.example.filmpass.domain.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentConfirmRequestDto {

    @NotBlank(message = "주문 식별자는 빈값일 수 없습니다.")
    private String orderId;

    @NotBlank(message = "paymentKey 는 빈값일 수 없습니다.")
    private String paymentKey;

    @NotNull(message = "결제 요금은 빈값일 수 없습니다.")
    private Integer amount;

    public PaymentConfirmRequestDto(String orderId, String paymentKey, Integer amount) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }
}
