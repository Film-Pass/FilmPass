package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.dto.FindMovieRequest;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping("/{movieId}")
    public ApiResponse<Optional<Movie>> findMovieApi(@PathVariable Long movieId, @RequestBody FindMovieRequest findMovieRequest) {
        return movieService.findMovie(movieId, findMovieRequest);
    }
}
