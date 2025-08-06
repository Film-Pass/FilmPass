package com.example.filmpass.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCalculationResponseDto {

    private String orderId;
    private String paymentKey;
    private Integer amount;

}
