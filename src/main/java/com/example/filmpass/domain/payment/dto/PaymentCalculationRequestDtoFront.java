package com.example.filmpass.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentCalculationRequestDtoFront {

    @Schema(description = "할인전 요금", example = "30000")
    @NotNull(message = "금액은 빈값일 수 없습니다.")
    private Integer originalAmount;     // 할인 전

    @Schema(description = "할인 요금", example = "6000")
    @NotNull(message = "금액은 빈값일 수 없습니다.")
    private Integer discountAmount;     // 할인된 금액

    @Schema(description = "최종 결제요금", example = "24000")
    @NotNull(message = "금액은 빈값일 수 없습니다.")
    private Integer finalAmount;        // 최종 결제 금액

    @Schema(description = "할인 사유", example = "국가 유공자 20% 할인")
    @NotBlank(message = "할인사유는 빈값일 수 없습니다.")
    private String discountReason;

    @Schema(description = "주문 식별자", example = "응답에서 복붙 필요")
    @NotBlank(message = "주문 식별자는 빈값일 수 없습니다.")
    private String orderId;

}
