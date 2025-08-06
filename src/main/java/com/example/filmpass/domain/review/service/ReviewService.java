package com.example.filmpass.domain.review.service;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.entity.Review;
import com.example.filmpass.domain.review.repository.ReviewRepository;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    // 리뷰 생성
    public ReviewResponseDto createReview(ReviewRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        Review review = new Review(
                request.getRating(),
                request.getContent(),
                movie,
                user
        );

        Review saved = reviewRepository.save(review);
        //평균 평점 재계산
       List<Review> reviewList = reviewRepository.findAllByMovieId(request.getMovieId());
        int reivewCount = reviewList.size();
        double reivesRating = reviewList.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        double avrRating = Math.round(reivesRating * 10.0) / 10.0;
        movie.updateRating(request.getMovieId(), avrRating, reivewCount);

        return ReviewResponseDto.from(saved);
    }

    // 리뷰 수정
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto request) {
        Review review = reviewRepository.findByReviewIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        review.update(request.getRating(), request.getContent(), movie);
        return ReviewResponseDto.from(review);
    }

    // 리뷰 목록 조회
    public Page<ReviewResponseDto> getReviewsByMovie(Long movieId, Pageable pageable) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        return reviewRepository.findAllByMovieAndIsDeletedFalse(movie, pageable)
                .map(ReviewResponseDto::from);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findByReviewIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        review.softDelete();
        reviewRepository.save(review);
    }
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }
}