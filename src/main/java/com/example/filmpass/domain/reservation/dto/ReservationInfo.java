package com.example.filmpass.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationInfo {
    private Long reservationId;
    private String seatNumber;
}
