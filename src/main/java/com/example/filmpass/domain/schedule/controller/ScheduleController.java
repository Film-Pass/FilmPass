package com.example.filmpass.domain.schedule.controller;

import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<String> createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        scheduleService.createSchedule(requestDto);
        return ResponseEntity.ok("상영 일정이 등록되었습니다.");
    }
}
