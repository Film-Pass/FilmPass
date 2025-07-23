package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.dto.FindMovieDetailResponse;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MoviceRepository;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MoviceRepository moviceRepository;

    //영화 상세 조회
    public ApiResponse<Object> findMovieDtail(Long movieId) {
        Optional<Movie> optionalMovie = moviceRepository.findById(movieId);
        if(optionalMovie.isEmpty()) {
            return ApiResponse.error("Id에 해당하는 영화를 찾을 수 없습니다.");
        }

        Movie movie = optionalMovie.get();
        FindMovieDetailResponse findMovieDetailResponse = new FindMovieDetailResponse(movie.getTitle(), movie.getDirector(), movie.getDescription());

        return ApiResponse.success(findMovieDetailResponse, "영화 상세 조회 성공");
    }
}
