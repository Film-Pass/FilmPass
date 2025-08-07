package com.example.filmpass.domain.schedule.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleRequestDto {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long screenId;
    private Long movieId;

    public ScheduleRequestDto(Long screenId, Long movieId, LocalDateTime startAt, LocalDateTime endAt) {
        this.screenId = screenId;
        this.movieId = movieId;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}

