package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.dto.UpdateMovieRequest;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.domain.movie.dto.FindMovieRequest;
import com.example.filmpass.domain.movie.dto.MovieCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ApiResponse<List<Movie>> findAllMovieApi() {
       return movieService.findAllMovie();
    }

    //영화 검색
    @PostMapping("/{movieId}")
    public ApiResponse<Optional<Movie>> findMovieApi(@PathVariable Long movieId, @RequestBody FindMovieRequest findMovieRequest) {
        return movieService.findMovie(movieId, findMovieRequest);
    }

    //영화 수정
    @PatchMapping("/{movieId}")
    public ApiResponse<Movie> updateMovieApi(@PathVariable Long movieId, @RequestBody UpdateMovieRequest updateMovieRequest) {
        return movieService.updateMovie(movieId, updateMovieRequest);
    }

    //영화 상세 조회
    @GetMapping("/{movieId}")
    public ApiResponse<Object> findMovieDetail(@PathVariable Long movieId) {
        return movieService.findMovieDtail(movieId);
    }
}
