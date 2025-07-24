package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.dto.FindMovieRequest;
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

    //영화 검색
    public ApiResponse<Optional<Movie>> findMovie(Long movieId, FindMovieRequest findMovieRequest) {
        Long id = movieId;
        String title = findMovieRequest.getTitle();
        String director = findMovieRequest.getDirector();

        if(id !=null) {
            Optional<Movie> movie = moviceRepository.findById(id);
            return ApiResponse.success(movie,"영화 검색 성공");
        }
        else if(title != null){
            Optional<Movie> movie = moviceRepository.findByTitle(title);
            return ApiResponse.success(movie,"영화 검색 성공");
        }
        else if(director != null){
            Optional<Movie> movie = moviceRepository.findByDirector(director);
            return ApiResponse.success(movie,"영화 검색 성공");
        }
        return ApiResponse.error("입력값에 해당하는 영화가 존재하지 않습니다");
    }
}
