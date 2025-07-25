package com.example.filmpass.domain.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private String seat_Number;
}
