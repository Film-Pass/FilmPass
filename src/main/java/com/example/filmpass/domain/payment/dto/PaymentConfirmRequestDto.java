package com.example.filmpass.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentConfirmRequestDto {

    @Schema(description = "주문 식별자", example = "응답에서 복붙 필요")
    @NotBlank(message = "주문 식별자는 빈값일 수 없습니다.")
    private String orderId;

    @Schema(description = "결제 토큰", example = "응답에서 복붙 필요")
    @NotBlank(message = "paymentKey 는 빈값일 수 없습니다.")
    private String paymentKey;

    @Schema(description = "요금", example = "24000")
    @NotNull(message = "결제 요금은 빈값일 수 없습니다.")
    private Integer amount;

    public PaymentConfirmRequestDto(String orderId, String paymentKey, Integer amount) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }
}
