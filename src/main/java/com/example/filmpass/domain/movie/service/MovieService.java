package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.domain.movie.dto.MovieCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    //영화 등록 CreateMovie
    public ApiResponse<String> movieCreate(MovieCreateRequest movieCreateRequest){
        String runningTime = movieCreateRequest.getRunningTime();
        String director = movieCreateRequest.getDirector();
        String description = movieCreateRequest.getDescription();
        String posterUrl = movieCreateRequest.getMovieImage();
        String title = movieCreateRequest.getMovieName();

        Movie movie = new Movie(runningTime,director,description,posterUrl,title);
        movieRepository.save(movie);

        return ApiResponse.success(movie.getTitle(),"영화 등록 성공!");
    }

    //영화 전체 조회
    public ApiResponse<List<Movie>> findAllMovie() {
        List<Movie> movieList = movieRepository.findAll();
        if(movieList.isEmpty()) {
            return ApiResponse.success(movieList,"영화가 존재하지 않습니다");
        }
        return ApiResponse.success(movieList, "영화목록 조회 성공!");
    }
}
