package com.example.filmpass.domain.reservation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class CalculateAmountRequestDto {

    @NotEmpty(message = "하나 이상의 좌석을 선택해주세요.")
    List<Long> seatIds;

}
