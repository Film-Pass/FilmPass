package com.example.filmpass.schedule;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.dto.ScheduleResponseDto;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.schedule.service.ScheduleService;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.enums.ScreenType;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service: ScheduleService 테스트")
class ScheduleServiceTest {

    @Mock private ScheduleRepository scheduleRepository;
    @Mock private ScreenRepository screenRepository;
    @Mock private MovieRepository movieRepository;

    @InjectMocks private ScheduleService scheduleService;

    private Theater theater;
    private Screen screen;
    private Movie movie;

    @BeforeEach
    void setUp() {
        theater = new Theater("메가박스 강남", "서울시 강남구");
        theater.setIdForTest(1L); // 테스트용 ID 주입

        screen = new Screen("스크린1", "주소1", ScreenType.FOUR_DX, theater);
        screen.setIdForTest(1L); // 테스트용 ID 주입

        movie = new Movie("테스트 영화", "감독", "장르", "2시간", "2023-01-01", "설명", "posterUrl");
        movie.setIdForTest(1L); // 테스트용 ID 주입
    }

    @Test
    @DisplayName("스케줄 생성 성공")
    void createSchedule_success() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2025, 8, 6, 10, 0);
        LocalDateTime endAt = LocalDateTime.of(2025, 8, 6, 12, 0);
        ScheduleRequestDto request = new ScheduleRequestDto(screen.getId(), movie.getId(), startAt, endAt);

        when(screenRepository.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(scheduleRepository.findOverlappingSchedules(screen.getId(), startAt, endAt)).thenReturn(List.of());

        Schedule saved = new Schedule(startAt, endAt, screen, movie);
        saved.setIdForTest(100L);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(saved);

        // when
        ScheduleResponseDto result = scheduleService.createSchedule(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getScheduleId()).isEqualTo(100L);
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    @DisplayName("스케줄 생성 실패 - 중복 시간")
    void createSchedule_fail_overlap() {
        LocalDateTime startAt = LocalDateTime.of(2025, 8, 6, 10, 0);
        LocalDateTime endAt = LocalDateTime.of(2025, 8, 6, 12, 0);
        ScheduleRequestDto request = new ScheduleRequestDto(screen.getId(), movie.getId(), startAt, endAt);

        Schedule overlap = new Schedule(startAt.minusHours(1), endAt.plusHours(1), screen, movie);

        when(screenRepository.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(scheduleRepository.findOverlappingSchedules(screen.getId(), startAt, endAt)).thenReturn(List.of(overlap));

        assertThatThrownBy(() -> scheduleService.createSchedule(request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.SCHEDULE_OVERLAP.getMessage());
    }

    @Test
    @DisplayName("스케줄 생성 실패 - 스크린 없음")
    void createSchedule_fail_screenNotFound() {
        LocalDateTime startAt = LocalDateTime.of(2025, 8, 6, 10, 0);
        LocalDateTime endAt = LocalDateTime.of(2025, 8, 6, 12, 0);
        ScheduleRequestDto request = new ScheduleRequestDto(999L, movie.getId(), startAt, endAt);

        when(screenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.createSchedule(request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.SCREEN_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("스케줄 생성 실패 - 영화 없음")
    void createSchedule_fail_movieNotFound() {
        LocalDateTime startAt = LocalDateTime.of(2025, 8, 6, 10, 0);
        LocalDateTime endAt = LocalDateTime.of(2025, 8, 6, 12, 0);
        ScheduleRequestDto request = new ScheduleRequestDto(screen.getId(), 999L, startAt, endAt);

        when(screenRepository.findById(screen.getId())).thenReturn(Optional.of(screen));
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.createSchedule(request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.MOVIE_NOT_FOUND.getMessage());
    }
}
