package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MoviceRepository;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.domain.movie.dto.MovieCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
