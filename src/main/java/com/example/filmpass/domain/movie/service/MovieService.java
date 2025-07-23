package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.dto.FindMovieDetailResponse;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MoviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MoviceRepository moviceRepository;

    public void findMovieDtail(Long movieId) {
        Movie movie = moviceRepository.findById(movieId)
        FindMovieDetailResponse findMovieDetailResponse = new FindMovieDetailResponse()
    }
}
