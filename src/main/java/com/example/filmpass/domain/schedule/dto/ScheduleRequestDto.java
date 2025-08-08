package com.example.filmpass.domain.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleRequestDto {

    @Schema(description = "시작 시간", example = "2025-08-09T12:00:00")
    private LocalDateTime startAt;

    @Schema(description = "종료 시간", example = "2025-08-09T14:35:00")
    private LocalDateTime endAt;

    @Schema(description = "상영관 식별자", example = "1")
    private Long screenId;

    @Schema(description = "영화 식별자", example = "1")
    private Long movieId;

    public ScheduleRequestDto(Long screenId, Long movieId, LocalDateTime startAt, LocalDateTime endAt) {
        this.screenId = screenId;
        this.movieId = movieId;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}

