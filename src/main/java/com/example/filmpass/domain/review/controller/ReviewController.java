package com.example.filmpass.domain.review.controller;

import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.service.ReviewService;
import com.example.filmpass.global.common.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
public class ReviewController<CustomUserDetails> {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(
            @RequestBody ReviewRequestDto request,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Long userId = reviewService.getUserIdByUsername(username);
        ReviewResponseDto response = reviewService.createReview(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "리뷰가 등록되었습니다."));
    }



    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto request
    ) {
        ReviewResponseDto response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "리뷰가 수정되었습니다."));
    }

    // 영화별 리뷰 목록 조회
    @GetMapping("/{movieId}/reviewId")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getReviewsByMovie(
            @PathVariable Long movieId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByMovie(movieId, pageable);
        return ResponseEntity.ok(ApiResponse.success(reviews, "해당 영화 리뷰 목록 조회 완료"));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(null, "리뷰가 삭제되었습니다."));
    }
}
