package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    //영화 삭제
    public ApiResponse<Object> deleteMovie(Long movieId) {
        Optional<Movie> findMovie = movieRepository.findById(movieId);

        if(findMovie.isEmpty()) {
            ApiResponse.error("Id에 해당하는 영화가 없습니다");
        }
        Movie movie = findMovie.get();
        movieRepository.deleteById(movieId);

        return ApiResponse.success(movie, "영화가 성공적으로 삭제되었습니다.");
    }
}
