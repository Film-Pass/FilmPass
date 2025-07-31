package com.example.filmpass.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {

    @NotNull(message = "영화 ID는 필수입니다.")
    private Long movieId;

    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점까지만 가능합니다.")
    private Integer rating;

    private String content;

}
