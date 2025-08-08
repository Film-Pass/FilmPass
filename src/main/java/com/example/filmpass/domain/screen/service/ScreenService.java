package com.example.filmpass.domain.screen.service;

import com.example.filmpass.domain.screen.dto.ScreenRequestDto;
import com.example.filmpass.domain.screen.dto.ScreenResponseDto;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;

    // 상영관 생성
    @Transactional
    public ScreenResponseDto createScreen(ScreenRequestDto requestDto) {
        if (screenRepository.existsByNameAndTheaterId(requestDto.getName(), requestDto.getTheaterId())) {
            throw new CustomException(ErrorCode.SCREEN_ALREADY_EXISTS);
        }

        Theater theater = theaterRepository.findById(requestDto.getTheaterId())
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        Screen screen = new Screen(
                requestDto.getName(),
                requestDto.getAddress(),
                requestDto.getScreenType(),
                theater
        );

        Screen savedScreen = screenRepository.save(screen);
        return ScreenResponseDto.from(savedScreen);
    }

    @Transactional
    public ScreenResponseDto updateScreen(Long screenId, ScreenRequestDto requestDto) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        boolean existsDuplicate = screenRepository.existsByNameAndTheaterIdAndIdNot(
                requestDto.getName(),
                requestDto.getTheaterId(),
                screenId
        );
        if (existsDuplicate) {
            throw new CustomException(ErrorCode.SCREEN_ALREADY_EXISTS);
        }

        Theater theater = theaterRepository.findById(requestDto.getTheaterId())
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));
        screen.update(
                requestDto.getName(),
                requestDto.getAddress(),
                requestDto.getScreenType(),
                theater
        );

        return ScreenResponseDto.from(screen);
    }

}
