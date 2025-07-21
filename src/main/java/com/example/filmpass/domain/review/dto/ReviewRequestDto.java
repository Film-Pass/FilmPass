package com.example.filmpass.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {
    private Long movieId;
    private Integer rating;
    private String content;
    private String author;
}
