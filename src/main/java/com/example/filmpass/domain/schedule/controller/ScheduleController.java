package com.example.filmpass.domain.schedule.controller;

import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.dto.ScheduleResponseDto;
import com.example.filmpass.domain.schedule.service.ScheduleService;
import com.example.filmpass.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 상영 일정 등록
    @PostMapping
    public ResponseEntity<ApiResponse> createSchedule(@RequestBody @Valid ScheduleRequestDto requestDto) {
        ScheduleResponseDto response = scheduleService.createSchedule(requestDto);
        return ResponseEntity.ok(ApiResponse.success(response, "상영 일정이 등록되었습니다."));
    }


    // 상영 일정 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse> updateSchedule(@PathVariable Long scheduleId,
            @RequestBody @Valid ScheduleRequestDto requestDto) {
        ScheduleResponseDto response = scheduleService.updateSchedule(scheduleId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(response, "상영 일정이 수정되었습니다."));
    }
}
