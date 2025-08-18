package com.example.filmpass.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateAmountRequestDto {

    @Schema(description = "좌석 식별자 목록", example = "[1,2,3]")
    @NotEmpty(message = "하나 이상의 좌석을 선택해주세요.")
    List<Long> seatIds;

}
