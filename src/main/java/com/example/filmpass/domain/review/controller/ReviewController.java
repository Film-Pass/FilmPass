package com.example.filmpass.domain.review.controller;

import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.service.ReviewService;
import com.example.filmpass.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(@RequestBody ReviewRequestDto request) {
        ReviewResponseDto response = reviewService.createReview(request);
        ApiResponse<ReviewResponseDto> responseBody = ApiResponse.success(response, "리뷰가 등록되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    //리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto request
    ) {
        ReviewResponseDto response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "리뷰가 수정되었습니다."));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getAllReviews(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ReviewResponseDto> reviews = reviewService.getAllReviews(pageable);
        return ResponseEntity.ok(ApiResponse.success(reviews, "리뷰 목록이 조회되었습니다."));
    }

}
