package com.example.filmpass.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Valid
public class ReviewRequestDto {

    @Schema(description = "영화 식별자", example = "1")
    @NotNull(message = "영화 ID는 필수입니다.")
    private Long movieId;

    @Schema(description = "유저 식별자", example = "1")
    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;

    @Schema(description = "별점", example = "3")
    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점까지만 가능합니다.")
    private Integer rating;

    @Schema(description = "리뷰 내용", example = "평범한 영화였습니다.")
    private String content;

    public ReviewRequestDto(Long userId, Long movieId, Integer rating, String content) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.content = content;
    }
}

