package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    //영화 전체 조회
    public ApiResponse<List<Movie>> findAllMovie() {
        List<Movie> movieList = movieRepository.findAll();
        if(movieList.isEmpty()) {
            return ApiResponse.success(movieList,"영화가 존재하지 않습니다");
        }
        return ApiResponse.success(movieList, "영화목록 조회 성공!");
    }
}
