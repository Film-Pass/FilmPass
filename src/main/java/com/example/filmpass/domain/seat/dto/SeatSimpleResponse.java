package com.example.filmpass.domain.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatSimpleResponse {
    private Long id;
    private String seatNumber;
    private SeatStatus status;
}