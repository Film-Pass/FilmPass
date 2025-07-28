package com.example.filmpass.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationSummaryResponse {
    private Long reservationId;
    private String movieTitle;
    private LocalDateTime startAt;
    private String status;
}
