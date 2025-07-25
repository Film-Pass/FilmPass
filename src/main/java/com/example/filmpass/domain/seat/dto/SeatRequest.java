package com.example.filmpass.domain.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatRequest {
    private String seat_Number;    // 예: "A1"
    private Long screen_Id;        // 어느 상영관 소속인지

}
