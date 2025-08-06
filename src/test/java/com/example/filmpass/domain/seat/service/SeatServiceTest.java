package com.example.filmpass.domain.seat.service;

import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.screen.repository.ScreenRepository;
import com.example.filmpass.domain.seat.dto.SeatRequest;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.dto.SeatStatus;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.seat.repository.SeatRepository;
import com.example.filmpass.domain.theater.entity.Theater;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ScreenRepository screenRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    void 좌석_생성_성공() {
        // given
        List<SeatRequest> requests = List.of(new SeatRequest("A1", 1L));

        Theater theater = new Theater("CGV", "서울 강남구");
        Screen mockScreen = new Screen("1관", "3층", theater);

        given(screenRepository.findById(1L)).willReturn(Optional.of(mockScreen));
        given(seatRepository.save(any(Seat.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        List<SeatResponse> results = seatService.createSeats(requests);

        // then
        assertNotNull(results);
        assertFalse(results.isEmpty());

        SeatResponse firstResponse = results.get(0);
        assertEquals("A1", firstResponse.getSeatNumber());

        verify(screenRepository).findById(1L);
        verify(seatRepository).save(any(Seat.class));
    }

    @Test
    void 좌석_전체_조회_성공() {
        // given
        Theater theater = new Theater("CGV", "서울 강남구");
        ReflectionTestUtils.setField(theater, "id", 1L);

        Screen screen = new Screen("1관", "3층", theater);
        ReflectionTestUtils.setField(screen, "id", 1L);

        Seat seat1 = new Seat(screen, "A1");
        Seat seat2 = new Seat(screen, "A2");
        ReflectionTestUtils.setField(seat1, "id", 1L);
        ReflectionTestUtils.setField(seat2, "id", 2L);

        given(seatRepository.findAll()).willReturn(List.of(seat1, seat2));

        // when
        List<SeatResponse> results = seatService.getAllSeats();

        // then
        assertNotNull(results);
        assertEquals(2, results.size());

        SeatResponse first = results.get(0);
        assertEquals("A1", first.getSeatNumber());
        assertEquals(1L, first.getScreenId());
        assertEquals("1관", first.getScreenName());
        assertEquals(1L, first.getTheaterId());
        assertEquals("CGV", first.getTheaterName());
        assertEquals(SeatStatus.AVAILABLE, first.getStatus());

        verify(seatRepository).findAll();
    }

    @Test
    void 좌석_단건_조회_성공() {
        // given
        Long seatId = 10L;
        Theater theater = new Theater("CGV", "서울 강남구");
        Screen mockScreen = new Screen("1관", "3층", theater);

        Seat seat = new Seat(mockScreen, "A1");

        given(seatRepository.findById(seatId)).willReturn(Optional.of(seat));

        // when
        SeatResponse result = seatService.getSeatById(seatId);

        // then
        assertNotNull(result);
        assertEquals("A1", result.getSeatNumber());

        verify(seatRepository).findById(seatId);
    }

    @Test
    void 좌석_수정_성공() {
        // given
        Theater theater = new Theater("CGV", "서울 강남구");
        ReflectionTestUtils.setField(theater, "id", 1L);

        Screen oldScreen = new Screen("1관", "3층", theater);
        ReflectionTestUtils.setField(oldScreen, "id", 1L);

        Screen newScreen = new Screen("2관", "4층", theater);
        ReflectionTestUtils.setField(newScreen, "id", 2L);

        Seat seat = new Seat(oldScreen, "A1");
        ReflectionTestUtils.setField(seat, "id", 10L);

        SeatRequest request = new SeatRequest("B5", 2L); // 좌석번호 변경 + 상영관 변경

        given(seatRepository.findById(10L)).willReturn(Optional.of(seat));
        given(screenRepository.findById(2L)).willReturn(Optional.of(newScreen));
        given(seatRepository.existsByScreenIdAndSeatNumber(2L, "B5")).willReturn(false); // 중복 없음

        // when
        SeatResponse response = seatService.updateSeat(10L, request);

        // then
        assertNotNull(response);
        assertEquals(10L, response.getSeatId());
        assertEquals("B5", response.getSeatNumber());
        assertEquals(2L, response.getScreenId());
        assertEquals("2관", response.getScreenName());
        assertEquals(1L, response.getTheaterId());
        assertEquals("CGV", response.getTheaterName());

        verify(seatRepository).findById(10L);
        verify(screenRepository).findById(2L);
        verify(seatRepository).existsByScreenIdAndSeatNumber(2L, "B5");
    }

    @Test
    void 좌석_고장_변경_성공() {
        // given
        Theater theater = new Theater("CGV", "서울 강남구");
        ReflectionTestUtils.setField(theater, "id", 1L);

        Screen screen = new Screen("1관", "3층", theater);
        ReflectionTestUtils.setField(screen, "id", 1L);

        Seat seat = new Seat(screen, "A1");
        ReflectionTestUtils.setField(seat, "id", 10L);

        // 처음 상태는 AVAILABLE
        assertEquals(SeatStatus.AVAILABLE, seat.getStatus());

        given(seatRepository.findById(10L)).willReturn(Optional.of(seat));

        // when
        seatService.markAsBroken(10L);

        // then
        assertEquals(SeatStatus.BROKEN, seat.getStatus()); // 상태가 고장으로 바뀌었는지 확인
        verify(seatRepository).findById(10L);              // 조회는 호출됨
    }

    @Test
    void 좌석_사용_가능_상태로_복구_성공() {
        // given
        Theater theater = new Theater("CGV", "서울 강남구");
        ReflectionTestUtils.setField(theater, "id", 1L);

        Screen screen = new Screen("1관", "3층", theater);
        ReflectionTestUtils.setField(screen, "id", 1L);

        Seat seat = new Seat(screen, "A1");
        ReflectionTestUtils.setField(seat, "id", 10L);

        // 초기 상태를 BROKEN으로 설정
        seat.markAsBroken();
        assertEquals(SeatStatus.BROKEN, seat.getStatus());

        given(seatRepository.findById(10L)).willReturn(Optional.of(seat));

        // when
        seatService.markAsAvailable(10L);

        // then
        assertEquals(SeatStatus.AVAILABLE, seat.getStatus()); // 복구되었는지 확인
        verify(seatRepository).findById(10L);                 // findById 호출 검증
    }
}