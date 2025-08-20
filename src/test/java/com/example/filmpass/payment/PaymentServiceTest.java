package com.example.filmpass.payment;

import com.example.filmpass.domain.payment.PaymentRespository;
import com.example.filmpass.domain.payment.PaymentService;
import com.example.filmpass.domain.payment.dto.PaymentCalculationRequestDtoFront;
import com.example.filmpass.domain.payment.dto.PaymentCalculationResponseDto;
import com.example.filmpass.domain.payment.dto.PaymentConfirmRequestDto;
import com.example.filmpass.domain.payment.dto.PaymentConfirmResponseDto;
import com.example.filmpass.domain.payment.entity.Payment;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRespository paymentRespository;

    @Mock
    private UserRepository userRepository; // Payment 용 유저 레포지토리 라고 가정


    // 결제 진행 - 성공
    // 10% 확률로 실패할 경우 또한 계산
    @Test
    public void 결제시_결제정보가_정상적으로_입력된다() {

        // given - 로그인된 User, Userprincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        PaymentCalculationRequestDtoFront requestDto = new PaymentCalculationRequestDtoFront(
                30000,
                3000,
                27000,
                "장애인 할인",
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        try {
            PaymentCalculationResponseDto response = paymentService.createPaymentKey(requestDto, principal);


            // then
            assertThat(response.getOrderId()).isEqualTo("ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7");
            assertThat(response.getAmount()).isEqualTo(27000);
            assertThat(response.getPaymentKey()).isNotNull();

        } catch (CustomException e) {

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PAYMENT_FAILED);

        }


    }

    // 결제 진행 - 존재하지 않는 유저를 조회
    @Test
    public void 결제진행을_하려는_유저가_존재하지_않으면_CustomException이_발생한다() {

        // given - UserPrincipal, requestDto, repository 행위
        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        PaymentCalculationRequestDtoFront requestDto = new PaymentCalculationRequestDtoFront(
                30000,
                3000,
                27000,
                "장애인 할인",
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> paymentService.createPaymentKey(requestDto, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

    // 결제 검증 - 성공
    @Test
    public void 결제검증시_응답에_모든_필드가_정상적으로_입력된다() {

        // given - Payment, requestDto, repository 행위
        Payment payment = new Payment(
                "tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                27000,
                LocalDateTime.now()
        );

        PaymentConfirmRequestDto requestDto = new PaymentConfirmRequestDto(
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                "tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                27000
        );

        when(paymentRespository.findByOrderId("ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7"))
                .thenReturn(Optional.of(payment));


        // when
        PaymentConfirmResponseDto response = paymentService.confirmPaymentAtPG(requestDto);

        // then
        assertThat(response.getAmount()).isEqualTo(27000);
        assertThat(response.getPaymentKey()).isEqualTo("tok3ee92907-cb5f-4c7d-a821-de174d1a75d9");
        assertThat(response.getOrderId()).isEqualTo("ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7");
        assertThat(response.getApprovedAt()).isNotNull();

    }

    // 결제 검증 - 존재하지 않는 주문번호
    @Test
    public void 결제검증시_주문번호에_해당하는_결제내역이_존재하지_않으면_CustomException이_발생한다() {

        // given - requestDto, repository 행위

        PaymentConfirmRequestDto requestDto = new PaymentConfirmRequestDto(
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                "tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                27000
        );

        when(paymentRespository.findByOrderId("ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7")).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> paymentService.confirmPaymentAtPG(requestDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);

    }

    // 결제 검증 - 위조된 결제 토큰으로 검증 요청
    @Test
    public void 결제검증시_위조된_결제토큰으로_검증시도시_CustomException이_발생한다() {

        // given - Payment, requestDto, repository 행위
        Payment payment = new Payment(
                "tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                27000,
                LocalDateTime.now()
        );

        PaymentConfirmRequestDto requestDto = new PaymentConfirmRequestDto(
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                "(위조)tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                27000
        );

        when(paymentRespository.findByOrderId("ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7")).thenReturn(Optional.of(payment));

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> paymentService.confirmPaymentAtPG(requestDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PAYMENTKEY_MISMATCH);

    }

    // 결제 검증 - 위조된 결제요금으로 검증 요청
    @Test
    public void 결제검증시_위조된_결제요금으로_검증시도시_CustomException이_발생한다() {

        // given - Payment, requestDto, repository 행위
        Payment payment = new Payment(
                "tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                27000,
                LocalDateTime.now()
        );

        PaymentConfirmRequestDto requestDto = new PaymentConfirmRequestDto(
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                "tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                27000000
        );

        when(paymentRespository.findByOrderId("ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7")).thenReturn(Optional.of(payment));

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> paymentService.confirmPaymentAtPG(requestDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.AMOUNT_MISMATCH);

    }


}
