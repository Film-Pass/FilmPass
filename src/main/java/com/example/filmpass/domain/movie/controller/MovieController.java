package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.dto.MovieCreateRequest;
import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
