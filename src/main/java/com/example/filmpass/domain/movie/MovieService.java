package com.example.filmpass.domain.movie;

import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import io.micrometer.common.util.StringUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    //영화 등록 CreateMovie
    @Transactional
    public ApiResponse<String> movieCreate(MovieCreateRequest movieCreateRequest){
        String runningTime = movieCreateRequest.getRunningTime();
        String director = movieCreateRequest.getDirector();
        String description = movieCreateRequest.getDescription();
        String posterUrl = movieCreateRequest.getMovieImage();
        String title = movieCreateRequest.getMovieName();

        validRequest(title, director, runningTime);

        movieRepository.findByTitle(title)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_ALREADY_EXISTS));

        Movie movie = new Movie(title,director,description,runningTime,posterUrl);
        movieRepository.save(movie);

        return ApiResponse.success(movie.getTitle(),"영화 등록 성공!");
    }

    private void validRequest(String title, String director, String runningTime) {
        if (StringUtils.isBlank(title)) {
            throw new CustomException(ErrorCode.MOVIE_TITLE_REQUIRED);
        }
        if (StringUtils.isBlank(director)) {
            throw new CustomException(ErrorCode.MOVIE_DIRECTOR_REQUIRED);
        }
        if (StringUtils.isBlank(runningTime)) {
            throw new CustomException(ErrorCode.MOVIE_RUNNING_TIME_REQUIRED);
        }
    }

    //영화 전체 조회
    @Transactional(readOnly = true)
    public ApiResponse<FindMovieResponse<Movie>> findAllMovie(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);

        if(moviePage.isEmpty()) {
            throw new CustomException(ErrorCode.MOVIE_LIST_NOT_FOUND);
        }

        PageInfo pageInfo = new PageInfo(moviePage.getNumber(), moviePage.getTotalPages(), moviePage.getTotalElements(), moviePage.getSize(), moviePage.isLast());
        FindMovieResponse<Movie> findMovieResponse = new FindMovieResponse(moviePage.getContent(), pageInfo);
        return ApiResponse.success(findMovieResponse, "영화 조회 성공!");
    }

    //영화 검색
    @Transactional(readOnly = true)
    public ApiResponse<Movie> findMovie(FindMovieRequest findMovieRequest) {
        Long id = findMovieRequest.getId();
        String title = findMovieRequest.getTitle();
        String director = findMovieRequest.getDirector();

        if (id != null) {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND_BY_ID));
            return ApiResponse.success(movie, "영화 검색 성공");
        } else if (title != null && !title.trim().isEmpty()) {
            Movie movie = movieRepository.findByTitle(title)
                    .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND_BY_TITLE));
            return ApiResponse.success(movie, "영화 검색 성공");
        } else if (director != null && !director.trim().isEmpty()) {
            Movie movie = movieRepository.findByDirector(director)
                    .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND_BY_DIRECTOR));
            return ApiResponse.success(movie, "영화 검색 성공");
        }

        return ApiResponse.error("id, 제목, 감독 중 하나 이상의 값을 입력해주세요");
    }

    //영화 수정
    @Transactional
    public ApiResponse<Movie> updateMovie(Long movieId, UpdateMovieRequest updateMovieRequest) {
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
        return ApiResponse.success(alreadyMovie, "수정이 정상적으로 완료되었습니다.");
    }

    //영화 상세 조회
    @Transactional(readOnly = true)
    public FindMovieDetailResponse findMovieDetail(Long movieId) {
        Movie alreadyMovie = movieRepository.findById(movieId)
                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        return new FindMovieDetailResponse(alreadyMovie.getTitle(), alreadyMovie.getDirector(), alreadyMovie.getDescription());
    }

    //영화 삭제
    @Transactional
    public void deleteMovie(Long movieId) {
        movieRepository.findById(movieId)
                .orElseThrow(()-> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        movieRepository.deleteById(movieId);
    }
}