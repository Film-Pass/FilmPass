package com.example.filmpass.domain.schedule.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.MovieRepository;
import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.dto.ScheduleResponseDto;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;

    // 스케줄 등록
    public void createSchedule(ScheduleRequestDto requestDto) {
        Screen screen = screenRepository.findById(requestDto.getScreenId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        Movie movie = movieRepository.findById(requestDto.getMovieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));


        Schedule schedule = new Schedule(
                requestDto.getStartAt(),
                requestDto.getEndAt(),
                screen,
                movie
        );

        scheduleRepository.save(schedule);
    }
  
    // 스케줄 수정
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
}