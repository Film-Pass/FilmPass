package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.dto.*;
import com.example.filmpass.domain.movie.service.MovieService;
import com.example.filmpass.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    //영화 등록 CreateMovie
    @PostMapping
    @Operation(summary = "(관리자 전용) 영화 등록", description = "영화를 등록합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse movieCreateApi(@RequestBody MovieCreateRequest movieCreateRequest) {
        MovieCreateResponse movieCreateResponse = movieService.movieCreate(movieCreateRequest);
        return CommonResponse.success(movieCreateResponse,"영화 등록이 정상적으로 완료되었습니다.");
    }

    //영화 전체 조회
    @GetMapping
    @Operation(summary = "영화 목록 조회", description = "영화목록을 조회합니다.")
    public CommonResponse findAllMovieApi(Pageable pageable) {
        FindMovieResponse<SimpleFindMovieResponse> findMovieResponse = movieService.findAllMovie(pageable);
        return CommonResponse.success(findMovieResponse, "영화 조회가 정상적으로 완료되었습니다.");
    }

    //영화 검색
    @PostMapping("/search")
    @Operation(summary = "영화 검색", description = "영화를 검색합니다.")
    public CommonResponse findMovieApi(@RequestBody FindMovieRequest findMovieRequest, Pageable pageable) {
        FindMovieResponse<SimpleFindMovieResponse> findMovieResponse = movieService.findMovie(findMovieRequest, pageable);
        return CommonResponse.success(findMovieResponse, "영화 검색이 정상적으로 완료되었습니다.");
    }

    //영화 수정
    @PatchMapping("/{movieId}")
    @Operation(summary = "(관리자 전용) 영화 정보 수정", description = "영화의 정보를 수정합니다.")
    public CommonResponse updateMovieApi(@PathVariable Long movieId, @RequestBody UpdateMovieRequest updateMovieRequest) {
        UpdateMovieResponse updateMovieResponse = movieService.updateMovie(movieId, updateMovieRequest);
        return CommonResponse.success(updateMovieResponse, "수정이 정상적으로 완료되었습니다.");
    }

    //영화 상세 조회
    @GetMapping("/{movieId}")
    @Operation(summary = "영화 상세정보 조회", description = "영화 상세정보를 조회합니다.")
    public CommonResponse findMovieDetail(@PathVariable Long movieId) {
        FindMovieDetailResponse findMovieDetailResponse = movieService.findMovieDetail(movieId);
        return CommonResponse.success(findMovieDetailResponse, "영화 상세 조회 성공");
    }

    //영화 삭제
    @DeleteMapping("/{movieId}")
    @Operation(summary = "(관리자 전용) 영화 삭제", description = "영화를 삭제합니다.")
    public CommonResponse deleteMovieApi(@PathVariable Long movieId) {
        DeleteMovieResponse deleteMovieResponse = movieService.deleteMovie(movieId);
        return CommonResponse.success(deleteMovieResponse,"영화가 성공적으로 삭제되었습니다.");
    }

    //영화 전체 조회 (캐싱)
    @GetMapping("/v2")
    @Operation(summary = "(캐싱) 영화 목록 조회", description = "캐시에 저장된 영화 목록을 조회합니다.")
    public CommonResponse findAllMovieApiV2(Pageable pageable) {
        FindMovieResponse<SimpleFindMovieResponse> findMovieResponse = movieService.findAllMovieV2(pageable);
        return CommonResponse.success(findMovieResponse, "영화 조회가 정상적으로 완료되었습니다.");
    }

}