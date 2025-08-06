package com.example.filmpass.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentConfirmResponseDto {

    private String orderId;
    private String paymentKey;
    private Integer amount;
    private LocalDateTime approvedAt;

}
