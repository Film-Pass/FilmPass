package com.example.filmpass.domain.schedule.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.schedule.dto.ScheduleRequestDto;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;

    // 스케쥴 등록
    @Transactional
    public void createSchedule(ScheduleRequestDto requestDto) {
        Screen screen = screenRepository.findById(requestDto.getScreenId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상영관입니다.")).getScreen();
        Movie movie = movieRepository.findById(requestDto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화입니다."));

        Schedule schedule = new Schedule(
                requestDto.getStartAt(),
                requestDto.getEndAt(),
                screen,
                movie
        );

        scheduleRepository.save(schedule);
    }
}
