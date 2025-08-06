package com.example.filmpass.domain.seat.service;

import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.seat.dto.SeatRequest;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;

    // 좌석 등록
    @Transactional
    public List<SeatResponse> createSeats(List<SeatRequest> requests) {
        List<SeatResponse> responses = new ArrayList<>();

        for (SeatRequest request : requests) {
            Long screenId = request.getScreenId();
            String seatNumber = request.getSeatNumber();

            // 1. 상영관(Screen) 존재 여부 확인
            Screen screen = screenRepository.findById(screenId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

            // 2. 중복 좌석 체크
            if (seatRepository.existsByScreenIdAndSeatNumber(screenId, seatNumber)) {
                throw new CustomException(ErrorCode.DUPLICATE_SEAT_NUMBER);
            }

            // 3. 좌석 저장
            Seat saved = seatRepository.save(new Seat(screen, seatNumber));

            // 4. 응답 DTO 생성 및 추가
            responses.add(new SeatResponse(
                    saved.getId(),
                    saved.getSeatNumber(),
                    screen.getId(),
                    screen.getName(),
                    screen.getTheater().getId(),
                    screen.getTheater().getName(),
                    saved.getStatus()
            ));
        }

        // 6. 모든 좌석 등록 후, 응답 리스트 반환
        return responses;
    }

    // 좌석 목록 조회
    public List<SeatResponse> getAllSeats() {
        List<Seat> seats = seatRepository.findAll();
        List<SeatResponse> responses = new ArrayList<>();

        for (Seat seat : seats) {
            Screen screen = seat.getScreen();
            responses.add(new SeatResponse(
                    seat.getId(),
                    seat.getSeatNumber(),
                    screen.getId(),
                    screen.getName(),
                    screen.getTheater().getId(),
                    screen.getTheater().getName(),
                    seat.getStatus()
            ));
        }

        return responses;
    }

    // 좌석 단건 조회
    public SeatResponse getSeatById(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

        Screen screen = seat.getScreen();
        Theater theater = screen.getTheater();

        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                screen.getId(),
                screen.getName(),
                theater.getId(),
                theater.getName(),
                seat.getStatus()
        );
    }

    // 좌석 수정 (중복 체크 포함)
    @Transactional
    public SeatResponse updateSeat(Long seatId, SeatRequest request) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        boolean exists = seatRepository.existsByScreenIdAndSeatNumber(screen.getId(), request.getSeatNumber());
        if (exists && !seat.getSeatNumber().equals(request.getSeatNumber())) {
            throw new CustomException(ErrorCode.DUPLICATE_SEAT_NUMBER);
        }

        seat.update(screen, request.getSeatNumber());

        Theater theater = screen.getTheater();

        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                screen.getId(),
                screen.getName(),
                theater.getId(),
                theater.getName(),
                seat.getStatus()
        );
    }

    // 고장난 좌석으로 변경 (save() 호출 생략)
    @Transactional
    public void markAsBroken(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

        if (!seat.isBroken()) {
            seat.markAsBroken();
        }
    }

    // 고장 복구 (사용 가능 상태로 변경)
    @Transactional
    public void markAsAvailable(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

        if (seat.isBroken()) {
            seat.markAsAvailable();
        }
    }
}

