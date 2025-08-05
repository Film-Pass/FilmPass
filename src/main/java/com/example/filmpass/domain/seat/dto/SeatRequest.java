package com.example.filmpass.domain.seat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatRequest {

    @NotNull(message = "좌석 번호는 필수입니다.")
    private String seatNumber;  // 예: "A1"

    @NotNull(message = "상영관 ID는 필수입니다.")
    private Long screenId;
}
