package com.example.filmpass.domain.schedule.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.dto.ScheduleResponseDto;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    // 스케줄 등록
    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {

        Screen screen = screenRepository.findById(requestDto.getScreenId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        Movie movie = movieRepository.findById(requestDto.getMovieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        List<Schedule> overlapped = scheduleRepository.findOverlappingSchedules(
                requestDto.getScreenId(), requestDto.getStartAt(), requestDto.getEndAt());

        if (!overlapped.isEmpty()) {
            throw new CustomException(ErrorCode.SCHEDULE_TIME_CONFLICT);
        }

        Schedule schedule = new Schedule(
                requestDto.getStartAt(),
                requestDto.getEndAt(),
                screen,
                movie
        );

        Schedule saved = scheduleRepository.save(schedule);
        return ScheduleResponseDto.from(saved);
    }


    // 스케줄 수정
    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleRequestDto request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        schedule.update(request.getStartAt(), request.getEndAt(), screen, movie);
        return ScheduleResponseDto.from(schedule);
    }

    // 스케줄 목록 조회
    public List<ScheduleResponseDto> getSchedules(Long theaterId, Long movieId) {

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        List<Screen> screens = screenRepository.findByTheater(theater);

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)
        );

        List<Schedule> schedules = scheduleRepository.findByScreenInAndMovieAndStartAtAfter(screens, movie, LocalDateTime.now());

        List<ScheduleResponseDto> schedulesDto = schedules.stream().map(ScheduleResponseDto::from).collect(Collectors.toList());

        return schedulesDto;
    }
}