package com.example.filmpass.domain.reservation.service;

import com.example.filmpass.domain.reservation.dto.ReservationInfo;
import com.example.filmpass.domain.reservation.dto.ReservationRequest;
import com.example.filmpass.domain.reservation.dto.ReservationResponse;
import com.example.filmpass.domain.reservation.entity.Reservation;
import com.example.filmpass.domain.reservation.repository.ReservationRepository;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public ReservationResponse reserve(Long userId, ReservationRequest request) {

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 2. 스케쥴 조회
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("상영 회차가 존재하지 않습니다."));

        // 3. 좌석 조회
        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new IllegalArgumentException("존재하지 않은 좌석이 포함되어 있습니다.");
        }

        // 4. 중복 예매 좌석 확인
        for (Seat seat : seats) {
            boolean reserved = reservationRepository.existsByScheduleAndSeat(schedule, seat);
            if (reserved) {
                throw new IllegalArgumentException("이미 예약된 좌석이 포함되어 있습니다" + seat.getSeat_Number());
            }
        }

        // 5. 예매 정보 저장 후 반환
        List<ReservationInfo> reservationInfos = new ArrayList<>();
        for (Seat seat : seats) {
            Reservation reservation = new Reservation(schedule, seat, user);
            reservationRepository.save(reservation);

            reservationInfos.add(new ReservationInfo(reservation.getId(), seat.getSeat_Number()));
        }

        return new ReservationResponse(schedule.getId(), reservationInfos);
    }
}
