package com.example.filmpass.domain.review.service;


import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.entity.Review;
import com.example.filmpass.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    //댓글 생성
    public ReviewResponseDto createReview(ReviewRequestDto request) {
        Review review = new Review(
                request.getMovieId(),
                request.getRating(),
                request.getContent(),
                request.getAuthor()
        );

        Review saved = reviewRepository.save(review);
        return ReviewResponseDto.from(saved);
    }
}
