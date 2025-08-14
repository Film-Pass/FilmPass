package com.example.filmpass.domain.theater.service;

import com.example.filmpass.domain.theater.dto.PagedResponse;
import com.example.filmpass.domain.theater.dto.TheaterRequest;
import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TheaterServiceTest {

    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private TheaterService theaterService;

    @Test
    void 극장_등록_성공() {
        // given
        TheaterRequest request = new TheaterRequest("CGV 강남", "서울시 강남구");
        Theater theater = new Theater(request.getName(), request.getLocation());
        ReflectionTestUtils.setField(theater, "id", 1L);

        given(theaterRepository.existsByName("CGV 강남")).willReturn(false);
        given(theaterRepository.save(any(Theater.class))).willReturn(theater);  // 이 부분 꼭 추가!

        // when
        TheaterResponse response = theaterService.createTheater(request);

        // then
        assertEquals(1L, response.getId());
        assertEquals("CGV 강남", response.getName());
        assertEquals("서울시 강남구", response.getLocation());
        verify(theaterRepository).existsByName("CGV 강남");
        verify(theaterRepository).save(any(Theater.class));
    }

    @Test
    void 극장_전체_조회_성공() {
        // given
        Theater t1 = new Theater("CGV", "서울");
        Theater t2 = new Theater("메가박스", "부산");
        ReflectionTestUtils.setField(t1, "id", 1L);
        ReflectionTestUtils.setField(t2, "id", 2L);

        List<Theater> theaterList = List.of(t1, t2);
        Page<Theater> page = new PageImpl<>(theaterList);

        Pageable pageable = PageRequest.of(0, 10);
        given(theaterRepository.findAll(pageable)).willReturn(page);

        // when
        PagedResponse<TheaterResponse> response = theaterService.getAllTheaters(pageable);

        // then
        assertEquals(2, response.getContent().size());
        assertEquals("CGV", response.getContent().get(0).getName());
        verify(theaterRepository).findAll(pageable);
    }

    @Test
    void 극장_단건_조회_성공() {
        // given
        Theater theater = new Theater("롯데시네마", "인천");
        ReflectionTestUtils.setField(theater, "id", 100L);
        given(theaterRepository.findById(100L)).willReturn(Optional.of(theater));

        // when
        TheaterResponse response = theaterService.getTheaterById(100L);

        // then
        assertEquals(100L, response.getId());
        assertEquals("롯데시네마", response.getName());
        assertEquals("인천", response.getLocation());
        verify(theaterRepository).findById(100L);
    }

    @Test
    void 극장_수정_성공() {
        // given
        Theater theater = new Theater("CGV", "서울");
        ReflectionTestUtils.setField(theater, "id", 1L);
        given(theaterRepository.findById(1L)).willReturn(Optional.of(theater));

        TheaterRequest request = new TheaterRequest("CGV 강남", "서울 강남구");

        // when
        TheaterResponse response = theaterService.updateTheater(1L, request);

        // then
        assertEquals("CGV 강남", response.getName());
        assertEquals("서울 강남구", response.getLocation());

        verify(theaterRepository).findById(1L);
    }
}