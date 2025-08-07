package com.example.filmpass.screen;

import com.example.filmpass.domain.screen.controller.ScreenController;
import com.example.filmpass.domain.screen.dto.ScreenRequestDto;
import com.example.filmpass.domain.screen.dto.ScreenResponseDto;
import com.example.filmpass.domain.screen.enums.ScreenType;
import com.example.filmpass.domain.screen.service.ScreenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScreenController.class)
class ScreenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreenService screenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("상영관 생성 성공")
    void createScreen_success() throws Exception {
        // given
        ScreenRequestDto requestDto = new ScreenRequestDto("스크린1", "서울 강남구", ScreenType.FOUR_DX, 1L);
        ScreenResponseDto responseDto = new ScreenResponseDto(1L, "스크린1", "서울 강남구", ScreenType.FOUR_DX, 1L);

        Mockito.when(screenService.createScreen(any(ScreenRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/screens")
                .with(csrf())
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("상영관이 등록되었습니다."))
                .andExpect(jsonPath("$.data.name").value("스크린1"))
                .andExpect(jsonPath("$.data.screenType").value("FOUR_DX"));
    }

    @Test
    @DisplayName("상영관 수정 성공")
    void updateScreen_success() throws Exception {
        // given
        Long screenId = 1L;
        ScreenRequestDto requestDto = new ScreenRequestDto("수정스크린", "서울 송파구", ScreenType.IMAX, 1L);
        ScreenResponseDto responseDto = new ScreenResponseDto(screenId, "수정스크린", "서울 송파구", ScreenType.IMAX, 1L);

        Mockito.when(screenService.updateScreen(eq(screenId), any(ScreenRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/screens/{screenId}", screenId)
                .with(csrf())
                .with(user("tester").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상영관이 수정되었습니다."))
                .andExpect(jsonPath("$.data.name").value("수정스크린"))
                .andExpect(jsonPath("$.data.screenType").value("IMAX"));
    }
}
