package com.example.filmpass.domain.reservation.service;

import com.example.filmpass.domain.payment.PaymentService;
import com.example.filmpass.domain.payment.dto.PaymentConfirmRequestDto;
import com.example.filmpass.domain.payment.dto.PaymentConfirmResponseDto;
import com.example.filmpass.domain.reservation.dto.*;
import com.example.filmpass.domain.reservation.entity.Reservation;
import com.example.filmpass.domain.reservation.repository.ReservationRepository;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.seat.dto.SeatStatus;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.DiscountType;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.config.UserPrincipal;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final RedissonService redissonService;
    private final PaymentService paymentService;
    private final UserService userService;
  
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
      
         // 4. 고장난 좌석 조회
        boolean hasBrokenSeat = seats.stream()
                .anyMatch(seat -> seat.getStatus() == SeatStatus.BROKEN);
        if (hasBrokenSeat) {
            throw new CustomException(ErrorCode.BROKEN_SEAT);
        }

        List<String> lockKeys = seats.stream().sorted()
                .map(seat -> "lock:schedule:" + schedule.getId() + ":seat:" + seat.getId())
                .toList();
        return redissonService.runWithMultiLock(lockKeys, 3, 10, () ->
            createReservation(user, schedule, seats)
        );
    }

    @Transactional
    protected ReservationResponse createReservation(User user, Schedule schedule, List<Seat> seats) {
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

    // 결제 요금 계산 로직
    public CalculateAmountResponseDto calculateAmounts(CalculateAmountRequestDto requestDto, UserPrincipal principal) {

        // 없는 좌석인지 검증
        List<Seat> seats = seatRepository.findAllById(requestDto.getSeatIds());
        if (seats.size() != requestDto.getSeatIds().size()) {
            throw new CustomException(ErrorCode.SEAT_NOT_FOUND);
        }

        // 유저 조회 (할인 적용을 위함)
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // screen 조회
        Screen screen = seats.get(0).getScreen();

        // 기본 가격 계산
        int originalAmount = screen.getAmount()*seats.size();
        int finalAmount = originalAmount;
        int discountAmount = originalAmount - finalAmount;
        String discountReason = "할인 없음";

        // 할인 적용 - 국가 유공자 20%, 장애인 10%
        if(user.getDiscountType().equals(DiscountType.PATRIOT)) {
            finalAmount = (int) (screen.getAmount()*seats.size()*0.8);
            discountAmount = originalAmount - finalAmount;
            discountReason = "국가 유공자 20% 할인";
        } else if (user.getDiscountType().equals(DiscountType.DISABLED)) {
            finalAmount = (int) (screen.getAmount()*seats.size()*0.9);
            discountAmount = originalAmount - finalAmount;
            discountReason = "장애인 10% 할인";
        }

        // orderId 생성
        String orderId = "ORDER" + UUID.randomUUID();

        return new CalculateAmountResponseDto(originalAmount, discountAmount, finalAmount, discountReason, orderId);

    }

    // 결제 확인 로직
    public String confirmPaymentAtBackend(PaymentConfirmRequestDtoFront requestDto, UserPrincipal principal) {

        // PG 사에 보낼 RequestBody
        PaymentConfirmRequestDto requestBody = new PaymentConfirmRequestDto(
                requestDto.getOrderId(),
                requestDto.getPaymentKey(),
                requestDto.getAmount()
        );

        // PG 사에 검증 요청을 보냈다고 가정
        // PG 사에서 검증 수행
        PaymentConfirmResponseDto paymentResponse = paymentService.confirmPaymentAtPG(requestBody);

        // 1% 만큼 포인트 적립
        int point = (int) (requestDto.getAmount()*0.01);
        userService.addPoint(principal.getUserId(), point);

        return "결제 검증 및 포인트 적립 성공!";

    }

}
