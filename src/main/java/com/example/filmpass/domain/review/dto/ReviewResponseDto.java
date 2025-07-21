package com.example.filmpass.domain.review.dto;

import com.example.filmpass.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private Long movieId;
    private Integer rating;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getReviewId(),
                review.getMovieId(),
                review.getRating(),
                review.getContent(),
                review.getAuthor(),
                review.getCreatedAt()
        );
    }
}
