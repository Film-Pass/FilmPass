package com.example.filmpass.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @Schema(description = "스케쥴 식별자", example = "1")
    private Long scheduleId;

    @Schema(description = "좌석 식별자 목록", example = "[1,2,3]")
    private List<Long> seatIds;
}
