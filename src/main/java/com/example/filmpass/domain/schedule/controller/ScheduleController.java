package com.example.filmpass.domain.schedule.controller;

import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.dto.ScheduleResponseDto;
import com.example.filmpass.domain.schedule.service.ScheduleService;
import com.example.filmpass.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "상영 일정 등록", description = "상영 일정을 등록합니다.")
    public ResponseEntity<CommonResponse> createSchedule(@RequestBody @Valid ScheduleRequestDto requestDto) {
        ScheduleResponseDto response = scheduleService.createSchedule(requestDto);
        return ResponseEntity.ok(CommonResponse.success(response, "상영 일정이 등록되었습니다."));
    }


    // 상영 일정 수정
    @PatchMapping("/{scheduleId}")
    @Operation(summary = "상영 일정 수정", description = "상영 일정 정보를 수정합니다.")
    public ResponseEntity<CommonResponse> updateSchedule(@PathVariable Long scheduleId,
                                                         @RequestBody @Valid ScheduleRequestDto requestDto) {
        ScheduleResponseDto response = scheduleService.updateSchedule(scheduleId, requestDto);
        return ResponseEntity.ok(CommonResponse.success(response, "상영 일정이 수정되었습니다."));
    }

    // 상영 일정 목록 조회
    @GetMapping("/{theaterId}/{movieId}")
    @Operation(summary = "상영 일정 조회", description = "상영 일정 목록을 조회합니다.")
    public ResponseEntity<CommonResponse> getSchedules(
            @PathVariable Long theaterId,
            @PathVariable Long movieId
    ) {

        return ResponseEntity.ok(CommonResponse.success(scheduleService.getSchedules(theaterId, movieId),"상영 일정 목록이 조회되었습니다."));

    }
}
