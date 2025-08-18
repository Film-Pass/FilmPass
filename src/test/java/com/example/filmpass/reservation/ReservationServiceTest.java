package com.example.filmpass.reservation;

import com.example.filmpass.domain.payment.PaymentService;
import com.example.filmpass.domain.reservation.dto.CalculateAmountRequestDto;
import com.example.filmpass.domain.reservation.dto.CalculateAmountResponseDto;
import com.example.filmpass.domain.reservation.dto.PaymentConfirmRequestDtoFront;
import com.example.filmpass.domain.reservation.repository.ReservationRepository;
import com.example.filmpass.domain.reservation.service.ReservationService;
import com.example.filmpass.domain.schedule.repository.ScheduleRepository;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.DiscountType;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.config.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;


    @Test
    public void 결제요금_계산시_응답에_모든_필드가_정상적으로_입력된다() {

        // given - 계산요청하는 User, UserPrincipal, 좌석 목록 - List<Long> List<Seat>, Screen, Theater requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setDiscountType(DiscountType.DISABLED);

        UserPrincipal principal = new UserPrincipal(1L, "사용자명", UserRole.GUEST);

        List<Long> seatIds = List.of(1L, 2L, 10L);

        Theater theater = new Theater("테스트 극장", "테스트 주소");
        Screen screen = new Screen("테스트 1관", "테스트 주소", theater);
        Seat seat1 = new Seat(screen, "A1");
        Seat seat2 = new Seat(screen, "A2");
        Seat seat10 = new Seat(screen, "A10");
        List<Seat> seats = List.of(seat1, seat2, seat10);

        CalculateAmountRequestDto requestDto = new CalculateAmountRequestDto(seatIds);

        when(seatRepository.findAllById(seatIds)).thenReturn(seats);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        CalculateAmountResponseDto response = reservationService.calculateAmounts(requestDto, principal);

        // then
        assertThat(response.getOriginalAmount()).isEqualTo(30000);
        assertThat(response.getDiscountAmount()).isEqualTo(3000);
        assertThat(response.getFinalAmount()).isEqualTo(27000);
        assertThat(response.getDiscountReason()).isEqualTo("장애인 10% 할인");
        assertThat(response.getOrderId()).isNotNull();

    }


    @Test
    public void 백엔드에서_결제검증시_응답이_정상적으로_생성된다() {

        // given - point 적립할 User, UserPrincipal, requestDto
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        UserPrincipal principal = new UserPrincipal(1L, "사용자명", UserRole.GUEST);

        PaymentConfirmRequestDtoFront requestDto = new PaymentConfirmRequestDtoFront(
                "ORDER51d75e3a-ab2b-48a5-8ea2-b2d05c933da7",
                "tok3ee92907-cb5f-4c7d-a821-de174d1a75d9",
                27000
        );

        // when
        String response = reservationService.confirmPaymentAtBackend(requestDto, principal);

        // then
        assertThat(response).isEqualTo("결제 검증 및 포인트 적립 성공!");

    }

}
