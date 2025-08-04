package com.example.filmpass.domain.seat.service;

import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.seat.dto.SeatRequest;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository; // 상영관 조회용

    // 좌석 등록
    @Transactional
    public List<SeatResponse> createSeats(List<SeatRequest> requests) {
        List<SeatResponse> responses = new ArrayList<>(); // 단일로만 아니라 여러 좌석을 등록하고자 함.

        // 요청받은 좌석 리스트를 하나씩 처리
        for (SeatRequest request : requests) {
            // 1. 상영관(Screen) 존재 여부 확인
            Screen screen = screenRepository.findById(request.getScreenId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

            // 2. 해당 상영관 내 좌석번호 중복 체크
            boolean exists = seatRepository.existsByScreenIdAndSeatNumber(screen.getId(), request.getSeatNumber());
            if (exists) {
                throw new CustomException(ErrorCode.DUPLICATE_SEAT_NUMBER);
            }

            // 3. 좌석 엔티티 생성 (상영관과 좌석번호로)
            Seat seat = new Seat(screen, request.getSeatNumber());

            // 4. DB에 좌석 저장
            Seat saved = seatRepository.save(seat);

            // 5. 저장된 좌석 정보를 응답용 DTO로 변환하여 리스트에 추가
            responses.add(new SeatResponse(
                    saved.getId(),
                    saved.getSeatNumber(),
                    screen.getId(),
                    screen.getName(),
                    screen.getTheater().getId(),
                    screen.getTheater().getName()
            ));
        }

        // 6. 모든 좌석 등록 완료 후, 등록된 좌석 리스트 반환
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
                    screen.getTheater().getName()
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
                theater.getName()
        );
    }

    // 좌석 수정
    @Transactional
    public SeatResponse updateSeat(Long seatId, SeatRequest request) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        seat.update(screen, request.getSeatNumber());

        Theater theater = screen.getTheater();

        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                screen.getId(),
                screen.getName(),
                theater.getId(),
                theater.getName()
        );
    }
}
