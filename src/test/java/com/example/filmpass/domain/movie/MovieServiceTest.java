package com.example.filmpass.domain.movie;

import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.domain.review.repository.ReviewRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("영화 등록 성공")
    void movieCreate_success() {
        // given
        MovieCreateRequest req = new MovieCreateRequest(
                "타이틀", "감독", "장르", "2시간", "2025-08-06", "줄거리", "https://poster.url"
        );
        when(movieRepository.findByTitle("타이틀")).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        MovieCreateResponse res = movieService.movieCreate(req);

        // then
        assertEquals("타이틀", res.getTitle());
        assertEquals("감독", res.getDirector());
        assertEquals("장르", res.getGenre());
        assertEquals("2시간", res.getRunningTime());
    }

    @Test
    @DisplayName("영화 등록 실패 - 중복 제목")
    void movieCreate_fail_duplicateTitle() {
        // given
        MovieCreateRequest req = new MovieCreateRequest(
                "타이틀", "감독", "장르", "2시간",
                "2025-08-06", "줄거리", "https://poster.url"
        );
        // public 생성자를 이용해 더미 Movie 객체 생성
        Movie duplicate = new Movie(
                "타이틀", "감독", "장르",
                "2시간", "2025-08-06",
                "줄거리", "https://poster.url"
        );
        when(movieRepository.findByTitle("타이틀"))
                .thenReturn(Optional.of(duplicate));

        // when & then
        CustomException ex = assertThrows(CustomException.class, () ->
                movieService.movieCreate(req)
        );
        assertEquals(ErrorCode.MOVIE_ALREADY_EXISTS, ex.getErrorCode());
    }


    @Test
    @DisplayName("영화 전체 조회 성공")
    void findAllMovie_success() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        Movie m1 = Mockito.mock(Movie.class);
        when(m1.getId()).thenReturn(1L);
        when(m1.getTitle()).thenReturn("무비1");
        when(m1.getGenre()).thenReturn("장르1");
        when(m1.getAvrRating()).thenReturn(3.0);
        when(m1.getReleaseDate()).thenReturn("2025-08-06");

        Movie m2 = Mockito.mock(Movie.class);
        when(m2.getId()).thenReturn(2L);
        when(m2.getTitle()).thenReturn("무비2");
        when(m2.getGenre()).thenReturn("장르2");
        when(m2.getAvrRating()).thenReturn(4.0);
        when(m2.getReleaseDate()).thenReturn("2025-08-07");

        Page<Movie> page = new PageImpl<>(List.of(m1, m2), pageable, 2);
        when(movieRepository.findAll(pageable)).thenReturn(page);

        // when
        FindMovieResponse<SimpleFindMovieResponse> res = movieService.findAllMovie(pageable);

        // then
        assertEquals(2, res.getData().size());
        SimpleFindMovieResponse first = res.getData().get(0);
        assertEquals(1L, first.getId());
        assertEquals("무비1", first.getTitle());
        assertEquals(3.0, first.getRating());
    }

    @Test
    @DisplayName("영화 검색 성공")
    void findMovie_success() {
        // given
        FindMovieRequest req = new FindMovieRequest(null, "기생충", null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Movie m = Mockito.mock(Movie.class);
        when(m.getId()).thenReturn(5L);
        when(m.getTitle()).thenReturn("기생충");
        when(m.getGenre()).thenReturn("드라마");
        when(m.getAvrRating()).thenReturn(4.5);
        when(m.getReleaseDate()).thenReturn("2019-05-30");

        Page<Movie> page = new PageImpl<>(List.of(m), pageable, 1);
        when(movieRepository.searchMoviesNative(
                eq(null), eq("기생충"), eq(null), eq(null), eq(pageable)
        )).thenReturn(page);

        // when
        FindMovieResponse<SimpleFindMovieResponse> res = movieService.findMovie(req, pageable);

        // then
        assertEquals(1, res.getData().size());
        SimpleFindMovieResponse dto = res.getData().get(0);
        assertEquals(5L, dto.getId());
        assertEquals("기생충", dto.getTitle());
    }

    @Test
    @DisplayName("영화 수정 성공")
    void updateMovie_success() {
        // given
        Long movieId = 1L;
        UpdateMovieRequest req = new UpdateMovieRequest(
                "수정 제목", "수정 감독", "수정 설명", "수정 상영시간", "수정 url", "수정 장르"
        );
        Movie movie = new Movie("old", "oldDir", "oldGen", "oldTime", "oldDate", "oldDesc", "oldUrl");
        movie.setIdForTest(movieId);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UpdateMovieResponse res = movieService.updateMovie(movieId, req);

        // then
        Movie updated = res.getMovie();
        assertEquals(movieId, updated.getId());
        assertEquals("수정 제목", updated.getTitle());
        assertEquals("수정 url", updated.getPosterUrl());
    }

    @Test
    @DisplayName("영화 상세 조회 성공")
    void findMovieDetail_success() {
        // given
        Long movieId = 3L;
        Movie movie = new Movie("타이틀", "감독", "장르", "시간", "2020-05-05", "설명", "URL");
        movie.setIdForTest(movieId);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // when
        FindMovieDetailResponse res = movieService.findMovieDtail(movieId);

        // then
        assertEquals(movieId, res.getId());
        assertEquals("타이틀", res.getMovieName());
        assertEquals("감독", res.getDirector());
    }

    @Test
    @DisplayName("영화 삭제 성공")
    void deleteMovie_success() {
        // given
        Long movieId = 9L;
        Movie movie = new Movie("삭제무비", "감독", "장르", "시간", "날짜", "설명", "URL");
        movie.setIdForTest(movieId);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // when
        DeleteMovieResponse res = movieService.deleteMovie(movieId);

        // then
        assertEquals("삭제무비", res.getMovieTitle());
    }
}
