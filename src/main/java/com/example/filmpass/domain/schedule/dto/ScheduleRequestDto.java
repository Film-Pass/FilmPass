package com.example.filmpass.domain.schedule.dto;

import jakarta.validation.Valid;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Valid
public class ScheduleRequestDto {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long screenId;
    private Long movieId;
}
