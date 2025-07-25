package com.example.filmpass.domain.seat.service;

import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.seat.dto.PagedResponse;
import com.example.filmpass.domain.seat.dto.SeatRequest;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import com.example.filmpass.domain.seat.dto.PagedResponse;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository; // 상영관 조회용

    // 좌석 등록
    public SeatResponse createSeat(SeatRequest request) {
        // 상영관 존재 확인
        Screen screen = screenRepository.findById(request.getScreen_Id())
                .orElseThrow(() ->  new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        // 2. 좌석 엔티티 생성
        Seat seat = new Seat(screen, request.getSeat_Number());

        // 저장
        Seat savedSeat = seatRepository.save(seat);

        return new SeatResponse(savedSeat.getId(), savedSeat.getSeat_Number());
    }

    // 좌석 목록 조회 (페이징)
    public PagedResponse<SeatResponse> getSeats(Pageable pageable) {
        Page<Seat> seatPage = seatRepository.findAll(pageable);

        List<SeatResponse> seatResponses = seatPage.getContent().stream()
                .map(seat -> new SeatResponse(seat.getId(), seat.getSeat_Number()))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                seatResponses,            // List<T> content
                seatPage.getNumber(),     // int page
                seatPage.getSize(),       // int size
                seatPage.getTotalElements(), // long totalElements
                seatPage.getTotalPages(), // int totalPages
                seatPage.isLast()
        );
    }
}
