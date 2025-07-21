package com.example.filmpass.domain.review.controller;

import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.service.ReviewService;
import com.example.filmpass.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(@RequestBody ReviewRequestDto request) {
        ReviewResponseDto response = reviewService.createReview(request);
        ApiResponse<ReviewResponseDto> responseBody = ApiResponse.success(response, "리뷰가 등록되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
