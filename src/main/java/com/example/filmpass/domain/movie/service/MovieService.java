package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    //영화 등록 CreateMovie
    @Transactional
    public MovieCreateResponse movieCreate(MovieCreateRequest movieCreateRequest){
        String runningTime = movieCreateRequest.getRunningTime();
        String director = movieCreateRequest.getDirector();
        String description = movieCreateRequest.getDescription();
        String posterUrl = movieCreateRequest.getMovieImage();
        String title = movieCreateRequest.getMovieName();

        if (title == null || title.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_TITLE_REQUIRED);
        }
        if (director == null || director.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_DIRECTOR_REQUIRED);
        }
        if (runningTime == null || runningTime.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_RUNNING_TIME_REQUIRED);
        }

        if(movieRepository.findByTitle(title).isPresent()){
            throw new CustomException(ErrorCode.MOVIE_ALREADY_EXISTS);
        }
        Movie movie = new Movie(title,director,description,runningTime,posterUrl);
        movieRepository.save(movie);

        return new MovieCreateResponse(movie.getTitle());
    }

    //영화 전체 조회
    @Transactional(readOnly = true)
    public FindMovieResponse<Movie> findAllMovie(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);

        if(moviePage.isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_LIST_NOT_FOUND);
        }
        PageInfo pageInfo = new PageInfo(moviePage.getNumber(), moviePage.getTotalPages(), moviePage.getTotalElements(), moviePage.getSize(), moviePage.isLast());
        return new FindMovieResponse<Movie> (moviePage.getContent(), pageInfo);
    }

    //영화 검색
    @Transactional
    public SearchMovieResponse findMovie(FindMovieRequest findMovieRequest, Pageable pageable) {
        Long id = findMovieRequest.getId();
        String title = findMovieRequest.getTitle();
        String director = findMovieRequest.getDirector();

        if (id == null) {
            if (title == null || title.trim().isEmpty()) {
                if (director == null || director.trim().isEmpty()) {
                    throw new CustomException(ErrorCode.MOVIE_SEARCH_REQUIRED);
                }
            }
        }

        if (title.trim().isEmpty()) {
            title=null;
        }
        if (director.trim().isEmpty()) {
            director = null;
        }
        Page<Movie> movies = movieRepository.searchMoviesNative(id, title, director, pageable);
        PageInfo pageInfo = new PageInfo(
                movies.getNumber(),
                movies.getTotalPages(),
                movies.getTotalElements(),
                movies.getSize(),
                movies.isLast()
        );
        return new SearchMovieResponse(movies.getContent(),pageInfo);
    }


    //영화 수정
    @Transactional
    public UpdateMovieResponse updateMovie(Long movieId, UpdateMovieRequest updateMovieRequest) {
        String newTitle = updateMovieRequest.getTitle();
        String newUrl = updateMovieRequest.getUrl();
        String newDescription = updateMovieRequest.getDescription();
        String newDirector = updateMovieRequest.getDirector();
        String newRunningTime = updateMovieRequest.getRunningTime();

        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_TITLE_REQUIRED);
        }
        if (newDirector == null || newDirector.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_DIRECTOR_REQUIRED);
        }
        if (newRunningTime == null || newRunningTime.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_RUNNING_TIME_REQUIRED);
        }
        Movie alreadyMovie = movieRepository.findById(movieId)
                .orElseThrow(()->new CustomException(ErrorCode.MOVIE_NOT_FOUND));


        alreadyMovie.updateMovie(newTitle, newUrl, newDescription, newDirector, newRunningTime);
        movieRepository.save(alreadyMovie);
        return new UpdateMovieResponse(alreadyMovie);
    }

    //영화 상세 조회
    @Transactional(readOnly = true)
    public FindMovieDetailResponse findMovieDtail(Long movieId) {
        Movie alreadyMovie = movieRepository.findById(movieId)
                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        return new FindMovieDetailResponse(alreadyMovie.getTitle(), alreadyMovie.getDirector(), alreadyMovie.getDescription());
    }

    //영화 삭제
    @Transactional
    public DeleteMovieResponse deleteMovie(Long movieId) {
        Movie alreadyMovie = movieRepository.findById(movieId)
                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        DeleteMovieResponse deleteMovieResponse = new DeleteMovieResponse(alreadyMovie.getTitle());
        movieRepository.deleteById(movieId);
        return deleteMovieResponse;
    }
}