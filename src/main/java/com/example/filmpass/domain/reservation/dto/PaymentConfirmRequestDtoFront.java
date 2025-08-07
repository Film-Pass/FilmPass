package com.example.filmpass.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentConfirmRequestDtoFront {

    @NotBlank(message = "orderId 는 빈값일 수 없습니다.")
    private String orderId;

    @NotBlank(message = "paymentKey 는 빈값일 수 없습니다.")
    private String paymentKey;

    @NotNull(message = "가격은 빈값일 수 없습니다.")
    private Integer amount;

}
