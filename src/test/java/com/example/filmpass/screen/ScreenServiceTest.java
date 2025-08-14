package com.example.filmpass.screen;

import com.example.filmpass.domain.screen.dto.ScreenRequestDto;
import com.example.filmpass.domain.screen.dto.ScreenResponseDto;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.enums.ScreenType;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.screen.service.ScreenService;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreenServiceTest {

    @InjectMocks
    private ScreenService screenService;

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private TheaterRepository theaterRepository;

    private Theater theater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        theater = new Theater("CGV강남", "서울시 강남구");
        theater.setIdForTest(1L);
    }

    @Test
    @DisplayName("상영관 생성 성공")
    void createScreen_success() {
        // given
        ScreenRequestDto request = new ScreenRequestDto("스크린1", "서울 강남구", ScreenType.THREE_D, 1L);

        when(screenRepository.existsByNameAndTheaterId("스크린1", 1L)).thenReturn(false);
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
        when(screenRepository.save(any(Screen.class))).thenAnswer(invocation -> {
            Screen screen = invocation.getArgument(0);
            screen.setIdForTest(1L);
            return screen;
        });

        // when
        ScreenResponseDto response = screenService.createScreen(request);

        // then
        assertThat(response.getName()).isEqualTo("스크린1");
        assertThat(response.getScreenType()).isEqualTo(ScreenType.THREE_D);
        assertThat(response.getTheaterId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상영관 생성 실패 - 중복된 이름")
    void createScreen_duplicateName() {
        // given
        ScreenRequestDto request = new ScreenRequestDto("스크린1", "서울 강남구", ScreenType.THREE_D, 1L);
        when(screenRepository.existsByNameAndTheaterId("스크린1", 1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> screenService.createScreen(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.SCREEN_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("상영관 수정 성공")
    void updateScreen_success() {
        // given
        Long screenId = 1L;
        ScreenRequestDto request = new ScreenRequestDto("변경스크린", "부산 해운대", ScreenType.FOUR_DX, 1L);

        Screen screen = new Screen("스크린1", "서울 강남구", ScreenType.THREE_D, theater);
        screen.setIdForTest(screenId);

        when(screenRepository.findById(screenId)).thenReturn(Optional.of(screen));
        when(screenRepository.existsByNameAndTheaterIdAndIdNot("변경스크린", 1L, screenId)).thenReturn(false);
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));

        // when
        ScreenResponseDto response = screenService.updateScreen(screenId, request);

        // then
        assertThat(response.getName()).isEqualTo("변경스크린");
        assertThat(response.getAddress()).isEqualTo("부산 해운대");
        assertThat(response.getScreenType()).isEqualTo(ScreenType.FOUR_DX);
    }
}
