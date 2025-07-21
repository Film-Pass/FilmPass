package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MoviceRepository;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MoviceRepository moviceRepository;

    //영화 목록 전체 조회
    public ApiResponse<List<Movie>> findAllMovie() {
        List<Movie> movieList = moviceRepository.findAll();
        return ApiResponse.success(movieList, "영화목록 조회 성공!");
    }
}
