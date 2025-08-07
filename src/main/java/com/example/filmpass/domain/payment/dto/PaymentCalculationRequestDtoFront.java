package com.example.filmpass.domain.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentCalculationRequestDtoFront {

    @NotNull(message = "금액은 빈값일 수 없습니다.")
    private Integer originalAmount;     // 할인 전

    @NotNull(message = "금액은 빈값일 수 없습니다.")
    private Integer discountAmount;     // 할인된 금액

    @NotNull(message = "금액은 빈값일 수 없습니다.")
    private Integer finalAmount;        // 최종 결제 금액

    @NotBlank(message = "할인사유는 빈값일 수 없습니다.")
    private String discountReason;

    @NotBlank(message = "주문 식별자는 빈값일 수 없습니다.")
    private String orderId;

}
