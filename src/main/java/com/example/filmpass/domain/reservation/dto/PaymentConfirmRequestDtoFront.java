package com.example.filmpass.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentConfirmRequestDtoFront {

    @Schema(description = "주문 식별자", example = "응답에서 복붙필요")
    @NotBlank(message = "orderId 는 빈값일 수 없습니다.")
    private String orderId;

    @Schema(description = "좌석 식별자 목록", example = "응답에서 복붙필요")
    @NotBlank(message = "paymentKey 는 빈값일 수 없습니다.")
    private String paymentKey;

    @Schema(description = "좌석 식별자 목록", example = "24000")
    @NotNull(message = "가격은 빈값일 수 없습니다.")
    private Integer amount;

}
