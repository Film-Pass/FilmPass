package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    //영화 삭제
    @DeleteMapping("/{movieId}")
    public ApiResponse<Object> deleteMovieApi(@PathVariable Long movieId) {
        return movieService.deleteMovie(movieId);
    }
}
