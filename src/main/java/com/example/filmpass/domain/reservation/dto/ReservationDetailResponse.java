package com.example.filmpass.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationDetailResponse {
    private Long reservationId;
    private Long movieId;
    private String movieTitle;
    private String posterUrl;
    private Long theaterId;
    private String theaterName;
    private String screenName;
    private String seatNumber;
    private LocalDateTime startAt;
    private LocalDateTime reservationAt;
    private SoftDeleteStatus status;
}
