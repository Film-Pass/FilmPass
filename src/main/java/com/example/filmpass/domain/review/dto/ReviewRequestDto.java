package com.example.filmpass.domain.review.dto;

import jakarta.validation.Valid;
import lombok.Getter;

@Getter
@Valid
public class ReviewRequestDto {
    private Long movieId;
    private Long userId;
    private Integer rating;
    private String content;
    private String author;
}