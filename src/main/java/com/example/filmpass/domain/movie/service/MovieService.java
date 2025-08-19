package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.review.entity.Review;
import com.example.filmpass.domain.review.repository.ReviewRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    //영화 등록
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public MovieCreateResponse movieCreate(MovieCreateRequest movieCreateRequest){
        String title = movieCreateRequest.getTitle();
        String director = movieCreateRequest.getDirector();
        String genre = movieCreateRequest.getGenre();
        String runningTime = movieCreateRequest.getRunningTime();
        String releaseDate = movieCreateRequest.getReleaseDate();
        String description = movieCreateRequest.getDescription();
        String posterUrl = movieCreateRequest.getPosterUrl();

        if (title == null || title.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_TITLE_REQUIRED);
        }
        if (director == null || director.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_DIRECTOR_REQUIRED);
        }
        if (runningTime == null || runningTime.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_RUNNING_TIME_REQUIRED);
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_GENRE_REQUIRED);
        }
        if(movieRepository.findByTitle(title).isPresent()){
            throw new CustomException(ErrorCode.MOVIE_ALREADY_EXISTS);
        }

        Movie movie = new Movie(title, director, genre, runningTime, releaseDate, description, posterUrl);
        movieRepository.save(movie);

        return new MovieCreateResponse(movie.getTitle(), movie.getDirector(), movie.getGenre(), movie.getRunningTime());
    }

    //영화 전체 조회
    @Transactional(readOnly = true)
    public FindMovieResponse<SimpleFindMovieResponse> findAllMovie(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        if (moviePage.isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_LIST_NOT_FOUND);
        }

        List<SimpleFindMovieResponse> simpleFindMovieResponseList = moviePage.stream()
                .map(movie -> new SimpleFindMovieResponse(
                        movie.getId(), movie.getTitle(), movie.getGenre(), movie.getAvrRating(), movie.getReleaseDate()
                ))
                .toList();

        PageInfo pageInfo = new PageInfo(moviePage.getNumber(), moviePage.getTotalPages(), moviePage.getTotalElements(), moviePage.getSize());
        return new  FindMovieResponse<SimpleFindMovieResponse> (simpleFindMovieResponseList, pageInfo);
    }

    //영화 검색
    @Transactional
    public FindMovieResponse<SimpleFindMovieResponse> findMovie(FindMovieRequest findMovieRequest, Pageable pageable) {
        Long id = findMovieRequest.getId();
        String title = findMovieRequest.getTitle();
        String director = findMovieRequest.getDirector();
        String genre = findMovieRequest.getGenre();

        if (id == null) {
            if (title == null || title.trim().isEmpty()) {
                if (director == null || director.trim().isEmpty()) {
                    if(genre == null || genre.trim().isEmpty()) {
                        throw new CustomException(ErrorCode.MOVIE_SEARCH_REQUIRED);
                    }
                }
            }
        }

        if (title != null && title.trim().isEmpty()) title = null;
        if (director != null && director.trim().isEmpty()) director = null;
        if (genre != null && genre.trim().isEmpty()) genre = null;

        Page<Movie> movies = movieRepository.searchMoviesNative(id, title, director, genre, pageable);
        if (movies.isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_SEARCH_NOT_FOUND);
        }

        List<SimpleFindMovieResponse> simpleFindMovieResponseList = movies.stream()
                .map(movie -> new SimpleFindMovieResponse(
                        movie.getId(), movie.getTitle(), movie.getGenre(), movie.getAvrRating(), movie.getReleaseDate()
                ))
                .toList();

        PageInfo pageInfo = new PageInfo(movies.getNumber(), movies.getTotalPages(), movies.getTotalElements(), movies.getSize());
        return new  FindMovieResponse<SimpleFindMovieResponse> (simpleFindMovieResponseList, pageInfo);
    }


    //영화 수정
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UpdateMovieResponse updateMovie(Long movieId, UpdateMovieRequest updateMovieRequest) {
        String newTitle = updateMovieRequest.getTitle();
        String newUrl = updateMovieRequest.getUrl();
        String newDescription = updateMovieRequest.getDescription();
        String newDirector = updateMovieRequest.getDirector();
        String newRunningTime = updateMovieRequest.getRunningTime();
        String newGenre = updateMovieRequest.getGenre();

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


        alreadyMovie.updateMovie(newTitle, newUrl, newDescription, newDirector, newRunningTime, newGenre);
        movieRepository.save(alreadyMovie);
        return new UpdateMovieResponse(alreadyMovie);
    }

    //영화 상세 조회
    @Transactional(readOnly = true)
    public FindMovieDetailResponse findMovieDetail(Long movieId) {
        Movie alreadyMovie = movieRepository.findById(movieId)

                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        return new FindMovieDetailResponse(alreadyMovie.getId(), alreadyMovie.getTitle(), alreadyMovie.getDirector(), alreadyMovie.getGenre(), alreadyMovie.getAvrRating(), alreadyMovie.getReviewCount(), alreadyMovie.getRunningTime(), alreadyMovie.getReleaseDate(), alreadyMovie.getDescription(), alreadyMovie.getPosterUrl());
    }

    //영화 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DeleteMovieResponse deleteMovie(Long movieId) {
        Movie alreadyMovie = movieRepository.findById(movieId)
                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        DeleteMovieResponse deleteMovieResponse = new DeleteMovieResponse(alreadyMovie.getTitle());
        alreadyMovie.deleteMovie();

        return deleteMovieResponse;
    }

    // 영화 전체 조회 (캐싱)
    @Cacheable(value = "movies:all", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public FindMovieResponse<SimpleFindMovieResponse> findAllMovieV2(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        List<SimpleFindMovieResponse> simpleFindMovieResponseList = moviePage.stream()
                .map(movie -> new SimpleFindMovieResponse(
                        movie.getId(), movie.getTitle(), movie.getGenre(), movie.getAvrRating(), movie.getReleaseDate()
                ))
                .toList();

        PageInfo pageInfo = new PageInfo(moviePage.getNumber(), moviePage.getTotalPages(), moviePage.getTotalElements(), moviePage.getSize());
        return new  FindMovieResponse<SimpleFindMovieResponse> (simpleFindMovieResponseList, pageInfo);
    }

    //영화 상세 조회 (캐싱)
    @Cacheable(value = "movies:detail", key = "#movieId")
    @Transactional(readOnly = true)
    public FindMovieDetailResponse findMovieDetailV2(Long movieId) {
        Movie alreadyMovie = movieRepository.findById(movieId)
                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        return new FindMovieDetailResponse(alreadyMovie.getId(), alreadyMovie.getTitle(), alreadyMovie.getDirector(), alreadyMovie.getGenre(), alreadyMovie.getAvrRating(), alreadyMovie.getReviewCount(), alreadyMovie.getRunningTime(), alreadyMovie.getReleaseDate(), alreadyMovie.getDescription(), alreadyMovie.getPosterUrl());
    }

    //영화 수정 (캐싱)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Caching(evict = {@CacheEvict(value = "movies:detail", key = "#movieId"),
                      @CacheEvict(value = "movies:all", allEntries = true),
                      @CacheEvict(value = "movies:search", allEntries = true)})
    public UpdateMovieResponse updateMovieV2(Long movieId, UpdateMovieRequest updateMovieRequest) {
        String newTitle = updateMovieRequest.getTitle();
        String newUrl = updateMovieRequest.getUrl();
        String newDescription = updateMovieRequest.getDescription();
        String newDirector = updateMovieRequest.getDirector();
        String newRunningTime = updateMovieRequest.getRunningTime();
        String newGenre = updateMovieRequest.getGenre();

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


        alreadyMovie.updateMovie(newTitle, newUrl, newDescription, newDirector, newRunningTime, newGenre);
        movieRepository.save(alreadyMovie);
        return new UpdateMovieResponse(alreadyMovie);
    }

    //영화 삭제 (캐싱)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Caching(evict = {@CacheEvict(value = "movies:detail", key = "#movieId"),
                      @CacheEvict(value = "movies:all", allEntries = true),
                      @CacheEvict(value = "movies:search", allEntries = true)})
    public DeleteMovieResponse deleteMovieV2(Long movieId) {
        Movie alreadyMovie = movieRepository.findById(movieId)
                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        DeleteMovieResponse deleteMovieResponse = new DeleteMovieResponse(alreadyMovie.getTitle());
        alreadyMovie.deleteMovie();

        return deleteMovieResponse;
    }

    //영화 등록 (캐싱)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Caching(evict = {@CacheEvict(value = "movies:all", allEntries = true),
                      @CacheEvict(value = "movies:search", allEntries = true)})
    public MovieCreateResponse movieCreateV2(MovieCreateRequest movieCreateRequest){
        String title = movieCreateRequest.getTitle();
        String director = movieCreateRequest.getDirector();
        String genre = movieCreateRequest.getGenre();
        String runningTime = movieCreateRequest.getRunningTime();
        String releaseDate = movieCreateRequest.getReleaseDate();
        String description = movieCreateRequest.getDescription();
        String posterUrl = movieCreateRequest.getPosterUrl();

        if (title == null || title.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_TITLE_REQUIRED);
        }
        if (director == null || director.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_DIRECTOR_REQUIRED);
        }
        if (runningTime == null || runningTime.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_RUNNING_TIME_REQUIRED);
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_GENRE_REQUIRED);
        }
        if(movieRepository.findByTitle(title).isPresent()){
            throw new CustomException(ErrorCode.MOVIE_ALREADY_EXISTS);
        }

        Movie movie = new Movie(title, director, genre, runningTime, releaseDate, description, posterUrl);
        movieRepository.save(movie);

        return new MovieCreateResponse(movie.getTitle(), movie.getDirector(), movie.getGenre(), movie.getRunningTime());
    }

    //영화 검색 (캐싱)
    @Transactional
    @Cacheable (value = "movies:search", key = "T(org.springframework.cache.interceptor.SimpleKeyGenerator).generateKey(" +
            "#findMovieRequest.id, " +
            "#findMovieRequest.title, " +
            "#findMovieRequest.director, " +
            "#findMovieRequest.genre, " +
            "#pageable.pageNumber, " +
            "#pageable.pageSize)"
    )
    public FindMovieResponse<SimpleFindMovieResponse> findMovieV2(FindMovieRequest findMovieRequest, Pageable pageable) {
        Long movieId = findMovieRequest.getId();
        String title = findMovieRequest.getTitle();
        String director = findMovieRequest.getDirector();
        String genre = findMovieRequest.getGenre();

        if (movieId == null) {
            if (title == null || title.trim().isEmpty()) {
                if (director == null || director.trim().isEmpty()) {
                    if(genre == null || genre.trim().isEmpty()) {
                        throw new CustomException(ErrorCode.MOVIE_SEARCH_REQUIRED);
                    }
                }
            }
        }

        if (title != null && title.trim().isEmpty()) title = null;
        if (director != null && director.trim().isEmpty()) director = null;
        if (genre != null && genre.trim().isEmpty()) genre = null;

        Page<Movie> movies = movieRepository.searchMoviesNative(movieId, title, director, genre, pageable);
        if (movies.isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_SEARCH_NOT_FOUND);
        }

        List<SimpleFindMovieResponse> simpleFindMovieResponseList = movies.stream()
                .map(movie -> new SimpleFindMovieResponse(
                        movie.getId(), movie.getTitle(), movie.getGenre(), movie.getAvrRating(), movie.getReleaseDate()
                ))
                .toList();

        PageInfo pageInfo = new PageInfo(movies.getNumber(), movies.getTotalPages(), movies.getTotalElements(), movies.getSize());
        return new  FindMovieResponse<SimpleFindMovieResponse> (simpleFindMovieResponseList, pageInfo);
    }
}