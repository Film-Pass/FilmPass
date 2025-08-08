package com.example.filmpass.domain.auth.dto;

import com.example.filmpass.domain.user.enums.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DiscountTypeRequestDto {

    @Schema(description = "변경할 할인 유형", example = "PATRIOT")
    @NotNull(message = "할인 타입을 입력해주세요.")
    private DiscountType discountType;

}
