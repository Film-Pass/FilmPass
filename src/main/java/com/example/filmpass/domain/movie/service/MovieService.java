package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.dto.FindMovieRequest;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MoviceRepository;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.domain.movie.dto.MovieCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MoviceRepository moviceRepository;

    //영화 등록 CreateMovie
    public ApiResponse<String> movieCreate(MovieCreateRequest movieCreateRequest){
        String runningTime = movieCreateRequest.getRunningTime();
        String director = movieCreateRequest.getDirector();
        String description = movieCreateRequest.getDescription();
        String posterUrl = movieCreateRequest.getMovieImage();
        String title = movieCreateRequest.getMovieName();

        Movie movie = new Movie(runningTime,director,description,posterUrl,title);
        moviceRepository.save(movie);

        return ApiResponse.success(movie.getTitle(),"영화 등록 성공!");
    }

    //영화 검색
    public ApiResponse<Optional<Movie>> findMovie(FindMovieRequest findMovieRequest) {
        Long id = findMovieRequest.getId();
        String title = findMovieRequest.getTitle();
        String director = findMovieRequest.getDirector();

        if(id !=null) {
            Optional<Movie> movie = moviceRepository.findById(id);
            return ApiResponse.success(movie,"영화 검색 성공");
        }

        if(title != null){
            Optional<Movie> movie = moviceRepository.findByTitle(title);
            return ApiResponse.success(movie,"영화 검색 성공");
        }

        if(director != null){
            Optional<Movie> movie = moviceRepository.findByDirector(director);
            return ApiResponse.success(movie,"영화 검색 성공");
        }
        return ApiResponse.error("입력값에 해당하는 영화가 존재하지 않습니다");
    }
}
