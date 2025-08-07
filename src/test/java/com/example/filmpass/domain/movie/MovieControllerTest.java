package com.example.filmpass.domain.movie;

import com.example.filmpass.domain.movie.controller.MovieController;
import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser  // 모든 테스트에 가짜 로그인 적용
@WebMvcTest(controllers = MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;  // ← 대체 어노테이션

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("영화 등록 성공")
    void movieCreate_success() throws Exception {
        MovieCreateRequest req = new MovieCreateRequest(
                "라라랜드", "데이미언 셔젤", "로맨스", "2시간 7분 40초", "2016년 12월 7일",
                "2016년 개봉한 데이미언 셔젤 감독의 뮤지컬 영화.","https://i.namu.wiki/i/78uTXq-Jd3ME_MYXtiyOo-qBPjwpiNF9qs1ko9YvE1BmaVagE9-h95a5Xuh0jVt6WX9sY8seQLZlU2GidF7Gcg.webp"
        );
        MovieCreateResponse res = new MovieCreateResponse("라라랜드", "데이미언 셔젤", "로맨스", "2시간 7분 40초");

        Mockito.when(movieService.movieCreate(any(MovieCreateRequest.class)))
                .thenReturn(res);

        mockMvc.perform(post("/api/movies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("라라랜드"))
                .andExpect(jsonPath("$.message").value("영화 등록이 정상적으로 완료되었습니다."));
    }

    @Test
    @DisplayName("영화 전체 조회 성공")
    void findAllMovie_success() throws Exception {
        PageRequest pageable = PageRequest.of(0, 5);
        List<SimpleFindMovieResponse> list = List.of(
                new SimpleFindMovieResponse(1L, "무비1", "장르1", 3.0, "2025년 8월 6일"),
                new SimpleFindMovieResponse(2L, "무비2", "장르2", 4.0, "2025년 8월 7일")
        );
        Page<SimpleFindMovieResponse> page = new PageImpl<>(list, pageable, list.size());

        PageInfo pageInfo = new PageInfo(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize()
        );
        FindMovieResponse<SimpleFindMovieResponse> res =
                new FindMovieResponse<>(page.getContent(), pageInfo);

        Mockito.when(movieService.findAllMovie(any(Pageable.class)))
                .thenReturn(res);

        mockMvc.perform(get("/api/movies")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("무비1"))
                .andExpect(jsonPath("$.data.content[0].genre").value("장르1"))
                .andExpect(jsonPath("$.data.content[0].rating").value(3.0))
                .andExpect(jsonPath("$.data.content[0].releaseDate").value("2025년 8월 6일"))
                .andExpect(jsonPath("$.data.content[1].id").value(2))
                .andExpect(jsonPath("$.data.content[1].title").value("무비2"))
                .andExpect(jsonPath("$.message")
                        .value("영화 조회가 정상적으로 완료되었습니다."));
    }

    @Test
    @DisplayName("영화 검색 성공")
    void searchMovie_success() throws Exception {
        FindMovieRequest req = new FindMovieRequest(null, "라라랜드", null, null);

        PageRequest pageable = PageRequest.of(0, 10);
        List<SimpleFindMovieResponse> list = List.of(
                new SimpleFindMovieResponse(5L, "라라랜드", "로맨스", 4.5, "2016년 12월 7일")
        );
        Page<SimpleFindMovieResponse> page = new PageImpl<>(list, pageable, list.size());

        PageInfo pageInfo = new PageInfo(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize()
        );
        FindMovieResponse<SimpleFindMovieResponse> res =
                new FindMovieResponse<>(page.getContent(), pageInfo);

        Mockito.when(movieService.findMovie(
                Mockito.any(FindMovieRequest.class),
                Mockito.any(Pageable.class))
        ).thenReturn(res);

        mockMvc.perform(post("/api/movies/search")
                        .with(csrf())
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].title").value("기생충"))
                .andExpect(jsonPath("$.data.pageInfo.totalElements").value(1))
                .andExpect(jsonPath("$.message")
                        .value("영화 검색이 정상적으로 완료되었습니다."));
    }

    @Test
    @DisplayName("영화 수정 성공")
    @WithMockUser(roles = "ADMIN")
    void updateMovie_success() throws Exception {
        Long movieId = 1L;
        UpdateMovieRequest req = new UpdateMovieRequest(
                "수정 제목",
                "수정 url",
                "수정 설명",
                "수정 감독",
                "수정 상영시간",
                "수정 장르"
        );

        Movie movie = Mockito.mock(Movie.class);
        Mockito.when(movie.getId()).thenReturn(movieId);
        Mockito.when(movie.getTitle()).thenReturn("수정 제목");
        Mockito.when(movie.getPosterUrl()).thenReturn("수정 url");
        Mockito.when(movie.getDescription()).thenReturn("수정 설명");
        Mockito.when(movie.getDirector()).thenReturn("수정 감독");
        Mockito.when(movie.getRunningTime()).thenReturn("수정 상영시간");
        Mockito.when(movie.getGenre()).thenReturn("수정 장르");
        UpdateMovieResponse res = new UpdateMovieResponse(movie);

        Mockito.when(movieService.updateMovie(
                eq(movieId),
                any(UpdateMovieRequest.class))
        ).thenReturn(res);
        mockMvc.perform(patch("/api/movies/{movieId}", movieId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.movie.id").value(movieId))
                .andExpect(jsonPath("$.data.movie.title").value("수정 제목"))
                .andExpect(jsonPath("$.data.movie.url").value("수정 url"))
                .andExpect(jsonPath("$.data.movie.description").value("수정 설명"))
                .andExpect(jsonPath("$.data.movie.director").value("수정 감독"))
                .andExpect(jsonPath("$.data.movie.runningTime").value("수정 상영시간"))
                .andExpect(jsonPath("$.data.movie.genre").value("수정 장르"))
                .andExpect(jsonPath("$.message")
                        .value("수정이 정상적으로 완료되었습니다."));
    }



    @Test
    @DisplayName("영화 상세 조회 성공")
    void findMovieDetail_success() throws Exception {
        FindMovieDetailResponse res = new FindMovieDetailResponse(3L, "상세조회 이름", "상세조회 감독", "상세조회 장르"
                , 5.0, 5, "상세조회 상영시간", "상세조회 개봉일", "상세조회 설명", "상세조회 url");

        Mockito.when(movieService.findMovieDtail(3L))
                .thenReturn(res);

        // 2) MockMvc 요청 및 JSON 응답 검증
        mockMvc.perform(get("/api/movies/{movieId}", 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // data 아래 모든 필드 검증
                .andExpect(jsonPath("$.data.id").value(3L))
                .andExpect(jsonPath("$.data.title").value("상세조회 이름"))
                .andExpect(jsonPath("$.data.director").value("상세조회 감독"))
                .andExpect(jsonPath("$.data.genre").value("상세조회 장르"))
                .andExpect(jsonPath("$.data.rating").value(5.0))
                .andExpect(jsonPath("$.data.reviewCount").value(5))
                .andExpect(jsonPath("$.data.runningTime").value("상세조회 상영시간"))
                .andExpect(jsonPath("$.data.releaseDate").value("상세조회 개봉일"))
                .andExpect(jsonPath("$.data.description").value("상세조회 설명"))
                .andExpect(jsonPath("$.data.posterUrl").value("상세조회 url"))
                .andExpect(jsonPath("$.message")
                        .value("영화 상세 조회 성공"));
    }


    @Test
    @DisplayName("영화 삭제 성공")
    @WithMockUser(roles = "ADMIN")
    void deleteMovie_success() throws Exception {
        DeleteMovieResponse res = new DeleteMovieResponse("테스트무비");
        Mockito.when(movieService.deleteMovie(9L))
                .thenReturn(res);
        mockMvc.perform(delete("/api/movies/{movieId}", 9L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("테스트무비"))
                .andExpect(jsonPath("$.message")
                        .value("영화가 성공적으로 삭제되었습니다."));
    }

}
