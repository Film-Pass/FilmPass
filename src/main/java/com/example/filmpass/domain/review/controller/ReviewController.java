package com.example.filmpass.domain.review.controller;

import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.service.ReviewService;
import com.example.filmpass.global.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    @Operation(summary = "리뷰 생성", description = "리뷰를 생성합니다.")
    public ResponseEntity<CommonResponse> createReview(@Valid @RequestBody ReviewRequestDto request) {
        ReviewResponseDto response = reviewService.createReview(request);
        CommonResponse responseBody = CommonResponse.success(response, "리뷰가 등록되었습니다.");
      
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }


    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰정보 수정", description = "작성한 리뷰의 정보를 수정합니다.")
    public ResponseEntity<CommonResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto request
    ) {
        ReviewResponseDto response = reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(CommonResponse.success(response, "리뷰가 수정되었습니다."));

    }

    // 영화별 리뷰 목록 조회
    @GetMapping("/{movieId}")
    @Operation(summary = "리뷰 목록 조회", description = "작성한 영화의 리뷰 목록을 조회합니다.")
    public ResponseEntity<CommonResponse> getReviewsByMovie(
            @PathVariable Long movieId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable

    ) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByMovie(movieId, pageable);
        return ResponseEntity.ok(CommonResponse.success(reviews, "해당 영화 리뷰 목록 조회 완료"));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    public ResponseEntity<CommonResponse> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(CommonResponse.success(null, "리뷰가 삭제되었습니다."));
    }

    //평론가가 쓴 평론
    @PostMapping("/critics")
    @Operation(summary = "평론가 리뷰 생성", description = "CRITIC 권한 사용자가 리뷰를 생성합니다.")
    @PreAuthorize("hasRole('CRITIC')")
    public ResponseEntity<CommonResponse> createCriticReview(@Valid @RequestBody ReviewRequestDto request) {
        ReviewResponseDto response = reviewService.createCriticReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(response, "평론가 리뷰가 등록되었습니다."));
    }

}