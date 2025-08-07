package com.example.filmpass.schedule;

import com.example.filmpass.domain.schedule.controller.ScheduleController;
import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.dto.ScheduleResponseDto;
import com.example.filmpass.domain.schedule.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("상영 일정 등록 성공")
    void createSchedule_success() throws Exception {
        // given
        LocalDateTime startAt = LocalDateTime.now().plusDays(1);
        LocalDateTime endAt = startAt.plusHours(2);
        Long screenId = 1L;
        Long movieId = 1L;

        ScheduleRequestDto requestDto = new ScheduleRequestDto(screenId, movieId, startAt, endAt);
        ScheduleResponseDto responseDto = new ScheduleResponseDto(100L, startAt, endAt, screenId, movieId);

        Mockito.when(scheduleService.createSchedule(any())).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/schedules")
                .with(csrf())
                .with(user("testUser").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.scheduleId").value(100L))
                .andExpect(jsonPath("$.message").value("상영 일정이 등록되었습니다."));
    }

    @Test
    @DisplayName("상영 일정 수정 성공")
    void updateSchedule_success() throws Exception {
        // given
        Long scheduleId = 100L;
        LocalDateTime startAt = LocalDateTime.now().plusDays(2);
        LocalDateTime endAt = startAt.plusHours(2);
        Long screenId = 1L;
        Long movieId = 1L;

        ScheduleRequestDto requestDto = new ScheduleRequestDto(screenId, movieId, startAt, endAt);
        ScheduleResponseDto responseDto = new ScheduleResponseDto(scheduleId, startAt, endAt, screenId, movieId);

        Mockito.when(scheduleService.updateSchedule(eq(scheduleId), any())).thenReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/schedules/{scheduleId}", scheduleId)
                .with(csrf())
                .with(user("testUser").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.scheduleId").value(scheduleId))
                .andExpect(jsonPath("$.message").value("상영 일정이 수정되었습니다."));
    }
}
