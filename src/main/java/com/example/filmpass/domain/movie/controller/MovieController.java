package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.domain.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    //영화 등록 CreateMovie
    @PostMapping
    public ApiResponse<Object> movieCreateApi(@RequestBody MovieCreateRequest movieCreateRequest) {
        MovieCreateResponse movieCreateResponse = movieService.movieCreate(movieCreateRequest);
        return ApiResponse.success(movieCreateResponse,"영솨 생성이 정상적으로 완료되었습니다.");
    }

    //영화 목록 조회
    @GetMapping
    public ApiResponse<Object> findAllMovieApi(Pageable pageable) {
        FindMovieResponse findMovieResponse = movieService.findAllMovie(pageable);
        return ApiResponse.success(findMovieResponse, "영화 조회가 정상적으로 완료되었습니다.");
    }

    //영화 검색
    @PostMapping("/search")
    public ApiResponse<Object> findMovieApi(@RequestBody FindMovieRequest findMovieRequest) {
        Page<Movie> moviePage = movieService.findMovie(findMovieRequest);
        return ApiResponse.success(moviePage, "영화 검색이 정상적으로 완료되었습니다.");
    }

    //영화 수정
    @PatchMapping("/{movieId}")
    public ApiResponse<Object> updateMovieApi(@PathVariable Long movieId, @RequestBody UpdateMovieRequest updateMovieRequest) {
        UpdateMovieResponse updateMovieResponse = movieService.updateMovie(movieId, updateMovieRequest);
        return ApiResponse.success(updateMovieResponse, "수정이 정상적으로 완료되었습니다.");
    }

    //영화 상세 조회
    @GetMapping("/{movieId}")
    public ApiResponse<Object> findMovieDetail(@PathVariable Long movieId) {
        FindMovieDetailResponse findMovieDetailResponse = movieService.findMovieDtail(movieId);
        return ApiResponse.success(findMovieDetailResponse, "영화 상세 조회 성공");
    }

    //영화 삭제
    @DeleteMapping("/{movieId}")
    public ApiResponse<Object> deleteMovieApi(@PathVariable Long movieId) {
        DeleteMovieResponse deleteMovieResponse = movieService.deleteMovie(movieId);
        return ApiResponse.success(deleteMovieResponse,"영화가 성공적으로 삭제되었습니다.");
    }
}