package com.example.filmpass.domain.auth.dto;

import com.example.filmpass.domain.user.enums.DiscountType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DiscountTypeRequestDto {

    @NotNull(message = "할인 타입을 입력해주세요.")
    private DiscountType discountType;

}
