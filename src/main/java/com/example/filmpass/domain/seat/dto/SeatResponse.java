package com.example.filmpass.domain.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatResponse {
    private Long seatId;
    private String seatNumber;
    private Long screenId;
    private String screenName;
    private Long theaterId;
    private String theaterName;
    private SeatStatus status;
}
