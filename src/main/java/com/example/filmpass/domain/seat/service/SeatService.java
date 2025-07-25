package com.example.filmpass.domain.seat.service;


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
