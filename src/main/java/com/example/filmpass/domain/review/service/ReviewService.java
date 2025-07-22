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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;  // UserRepository도 필요

    //리뷰 생성
    public ReviewResponseDto createReview(ReviewRequestDto request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Review review = new Review(
                request.getRating(),
                request.getContent(),
                movie,
                user
        );

        Review saved = reviewRepository.save(review);
        return ReviewResponseDto.from(saved);
    }

}
