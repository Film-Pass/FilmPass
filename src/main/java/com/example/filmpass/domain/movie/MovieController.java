package com.example.filmpass.domain.movie;

import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.domain.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    //영화 등록 CreateMovie
    @PostMapping
    public ApiResponse<String> movieCreateApi(@RequestBody MovieCreateRequest movieCreateRequest) {
        return movieService.movieCreate(movieCreateRequest);
    }

    //영화 목록 조회
    @GetMapping
    public ApiResponse<FindMovieResponse<Movie>> findAllMovieApi(Pageable pageable) {
        return movieService.findAllMovie(pageable);
    }

    //영화 검색
    @PostMapping("/search")
    public ApiResponse<Movie> findMovieApi(@RequestBody FindMovieRequest findMovieRequest) {
        return movieService.findMovie(findMovieRequest);
    }

    //영화 수정
    @PatchMapping("/{movieId}")
    public ApiResponse<Movie> updateMovieApi(@PathVariable Long movieId, @RequestBody UpdateMovieRequest updateMovieRequest) {
        return movieService.updateMovie(movieId, updateMovieRequest);
    }

    //영화 상세 조회
    @GetMapping("/{movieId}")
    public ApiResponse<FindMovieDetailResponse> findMovieDetail(@PathVariable Long movieId) {
        return ApiResponse.success(movieService.findMovieDetail(movieId), "영화 상세 조회 성공");
    }

    //영화 삭제
    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteMovieApi(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok().build();
    }
}