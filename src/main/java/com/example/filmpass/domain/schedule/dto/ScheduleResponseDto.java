package com.example.filmpass.domain.schedule.dto;

import com.example.filmpass.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long scheduleId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long screenId;
    private Long movieId;

    public static ScheduleResponseDto from(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getStartAt(),
                schedule.getEndAt(),
                schedule.getScreen().getId(),
                schedule.getMovie().getId()
        );
    }
}
