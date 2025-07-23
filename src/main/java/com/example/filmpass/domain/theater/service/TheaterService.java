package com.example.filmpass.domain.theater.service;


import com.example.filmpass.domain.theater.dto.TheaterRequest;
import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    // 극장 수정
    public TheaterResponse updateTheater(Long id, TheaterRequest request) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        theater.update(request.getName(), request.getLocation());

        return new TheaterResponse(
                theater.getId(),
                theater.getName(),
                theater.getLocation()
        );
    }
}
