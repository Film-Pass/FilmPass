package com.example.filmpass.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationDetailResponse {
    private Long reservationId;
    private String movieTitle;
    private String posterUrl;
    private String screenName;
    private String seatNumber;
    private LocalDateTime startAt;
    private LocalDateTime reservationAt;
    private String status;
}
