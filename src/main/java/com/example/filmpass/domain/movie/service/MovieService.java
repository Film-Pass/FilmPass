package com.example.filmpass.domain.movie.service;

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

    public ApiResponse<Movie> updateMovie(Long movieId, String newTitle, String newUrl, String newDescription, String newDirector, String newRunningTime) {
        Optional<Movie> findMovie = moviceRepository.findById(movieId);

        if (findMovie.isEmpty()) {
            return ApiResponse.error("Id에 해당하는 Movie가 없습니다.");
        }

        Movie movie = findMovie.get();
        movie.updateMovie(newTitle, newUrl, newDescription, newDirector, newRunningTime);

        moviceRepository.save(movie);
        return ApiResponse.success(movie, "수정이 정상적으로 완료되었습니다.");
    }
}
