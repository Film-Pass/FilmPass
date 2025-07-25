package com.example.filmpass.domain.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long screenId;
    private Long movieId;
}
