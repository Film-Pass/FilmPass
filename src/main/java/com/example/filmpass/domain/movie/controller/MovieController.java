package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PatchMapping("/{movieId}")
    public ApiResponse<Movie> updateMovieApi(@PathVariable Long movieId, @RequestBody String title, String url, String description, String director, String runningTime) {
        return movieService.updateMovie(movieId, title, url, description, director, runningTime);
    }

}
