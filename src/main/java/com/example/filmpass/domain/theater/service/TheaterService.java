package com.example.filmpass.domain.theater.service;


import com.example.filmpass.domain.theater.dto.PagedResponse;
import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;

import com.example.filmpass.domain.theater.dto.TheaterRequest;
import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    //극장 목록 조회
    public PagedResponse<TheaterResponse> getAllTheaters(Pageable pageable) {
        Page<Theater> page = theaterRepository.findAll(pageable);
        List<TheaterResponse> content = page.stream()
                .map(t -> new TheaterResponse(t.getId(), t.getName(), t.getLocation()))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }

    // 극장 단건 조회
    public TheaterResponse getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        return new TheaterResponse(
                theater.getId(),
                theater.getName(),
                theater.getLocation()
        );
    }

    private final TheaterRepository theaterRepository;

    // 극장 등록
    public TheaterResponse createTheater(TheaterRequest request) {
        // 1. 중복 이름 체크
        if (theaterRepository.existsByName(request.getName())) {
            throw new CustomException(ErrorCode.THEATER_ALREADY_EXISTS);
        }

        // 2. 극장 엔티티 생성
        Theater theater = new Theater(request.getName(), request.getLocation());

        // 3. 저장
        Theater savedTheater = theaterRepository.save(theater);

        // 4. 응답 DTO 변환 및 반환
        return new TheaterResponse(savedTheater.getId(), savedTheater.getName(), savedTheater.getLocation());
    }
}
