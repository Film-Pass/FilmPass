package com.example.filmpass.domain.auth.dto;

import com.example.filmpass.domain.user.enums.DiscountType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountTypeRequestDto {

    @NotNull(message = "할인 타입을 입력해주세요.")
    private DiscountType discountType;

}
