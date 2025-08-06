package com.example.filmpass.domain.reservation.service;

import com.example.filmpass.domain.reservation.dto.*;
import com.example.filmpass.domain.reservation.entity.Reservation;
import com.example.filmpass.domain.reservation.repository.ReservationRepository;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import com.example.filmpass.global.utility.RedissonService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final RedissonService redissonService;

    @Transactional  // 성능 문제 발생 / 대용량 트래픽이 넘어갈때 문제를 발생시킨다. / 깊게 공부해보자
    public ReservationResponse reserve(Long userId, ReservationRequest request) {

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2. 스케쥴 조회
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SCHEDULE));

        // 3. 좌석 조회
        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new CustomException(ErrorCode.SEAT_NOT_FOUND);
        }

        List<String> lockKeys = seats.stream()
                .map(seat -> "lock:schedule:" + schedule.getId() + ":" + ":seat:" + seat.getId())
                .toList();

        return redissonService.runWithMultiLock(lockKeys, 3, 5, () -> {
            final List<Reservation> reservedReservation = reservationRepository.findAllByScheduleAndSeatIn(schedule, seats);
            if (!reservedReservation.isEmpty()) {
                throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
            }

            final List<ReservationInfo> reservationInfos = new ArrayList<>();
            for (Seat seat : seats) {
                Reservation reservation = new Reservation(schedule, seat, user);
                reservationRepository.save(reservation);
                reservationInfos.add(new ReservationInfo(reservation.getId(), reservation.getSeat().getSeatNumber()));
            }

            return new ReservationResponse(schedule.getId(), reservationInfos);
        });
    }

    @Transactional
    public void cancelReservation(Long userId, Long reservationId) {

        // 1. 예매내역 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        // 2. 본인 확인
        if (!reservation.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_YOUR_RESERVATION);
        }

        // 3. 취소 여부 확인
        if (reservation.isSoftDeleted()) {
            throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
        }

        // 4. 플래그를 true 처리
        reservation.cancel();
    }

    @Transactional(readOnly = true)
    public ReservationDetailResponse getReservationDetail(Long userId, Long reservationId) {

        // 1. 예매 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        // 2. 본인 확인
        if (!reservation.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_YOUR_RESERVATION);
        }

        final Schedule schedule = reservation.getSchedule();
        return new ReservationDetailResponse(
                reservation.getId(),
                schedule.getMovie().getId(),
                schedule.getMovie().getTitle(),
                schedule.getMovie().getPosterUrl(),
                schedule.getScreen().getTheater().getId(),
                schedule.getScreen().getTheater().getName(),
                schedule.getScreen().getName(),
                reservation.getSeat().getSeatNumber(),
                schedule.getStartAt(),
                reservation.getReservationAt(),
                SoftDeleteStatus.of(reservation.isSoftDeleted())
        );
    }


    @Transactional
    public Page<ReservationSummaryResponse> getReservationList(Long userId, Pageable pageable) {

        Page<Reservation> reservations = reservationRepository.findByUserId(userId, pageable);

        return reservations.map(reservation -> {
            String movieTitle = reservation.getSchedule().getMovie().getTitle();
            LocalDateTime startAt = reservation.getSchedule().getStartAt();
            SoftDeleteStatus status = SoftDeleteStatus.of(reservation.isSoftDeleted());
            return new ReservationSummaryResponse(reservation.getId(), movieTitle, startAt, status);
        });
    }
}
