package com.example.filmpass.domain.payment;

import com.example.filmpass.domain.payment.dto.PaymentCalculationRequestDtoFront;
import com.example.filmpass.domain.payment.dto.PaymentCalculationResponseDto;
import com.example.filmpass.domain.payment.dto.PaymentConfirmRequestDto;
import com.example.filmpass.domain.payment.dto.PaymentConfirmResponseDto;
import com.example.filmpass.domain.payment.entity.Payment;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRespository paymentRespository;
    private final UserRepository userRepository; // Payment 용 유저 레포지토리 라고 가정


    // 결제 진행 로직
    public PaymentCalculationResponseDto createPaymentKey(PaymentCalculationRequestDtoFront requestDto, UserPrincipal principal) {

        // 존재하는 유저인지 검증
        userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 10% 확률로 결제 실패 발생
        int chance = ThreadLocalRandom.current().nextInt(100); // 0~99 중 하나 뽑음.
        if (chance < 10) {
            throw new CustomException(ErrorCode.PAYMENT_FAILED);
        }

        // paymentKey 생성
        String paymentKey = "tok" + UUID.randomUUID();

        // Payment 저장 - 할인된 금액을 검증후 amount 에 넣었다고 가정
        Payment payment = new Payment(paymentKey, requestDto.getOrderId(), requestDto.getFinalAmount(), LocalDateTime.now());
        paymentRespository.save(payment);

        return new PaymentCalculationResponseDto(payment.getOrderId(), payment.getPaymentKey(), payment.getAmount());
    }

    // 결제 검증 로직
    public PaymentConfirmResponseDto confirmPaymentAtPG(PaymentConfirmRequestDto requestDto) {

        // 결제내역 조회
        Payment payment = paymentRespository.findByOrderId(requestDto.getOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // PaymentKey 위조 여부 확인
        if(!payment.getPaymentKey().equals(requestDto.getPaymentKey())) {
            throw new CustomException(ErrorCode.PAYMENTKEY_MISMATCH);
        }

        // 금액 위조여부 확인
        if(!requestDto.getAmount().equals(payment.getAmount())) {
            throw new CustomException(ErrorCode.AMOUNT_MISMATCH);
        }

        // 응답 내보내기
        return new PaymentConfirmResponseDto(payment.getOrderId(), payment.getPaymentKey(), payment.getAmount(), payment.getApprovedAt());

    }

}
