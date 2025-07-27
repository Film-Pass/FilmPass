package com.example.filmpass.domain.reservation.dto;

import com.example.filmpass.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private Long scheduleId;
    private List<ReservationInfo> reservations;
}
