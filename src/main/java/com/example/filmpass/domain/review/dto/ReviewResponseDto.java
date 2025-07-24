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
                review.getMovie().getId(),        // movieId는 review.getMovie()에서 꺼내야 함
                review.getRating(),
                review.getContent(),
                review.getUser().getNickname(),   // author는 review.getUser()에서 꺼내야 함 (유저명 getter 메서드 이름에 맞게 수정)
                review.getCreatedAt()
        );
    }
}