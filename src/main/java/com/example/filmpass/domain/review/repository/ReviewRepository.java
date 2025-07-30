package com.example.filmpass.domain.review.repository;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.entity.Review;
import com.example.filmpass.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewIdAndIsDeletedFalse(Long reviewId);

    Page<Review> findAllByMovieAndIsDeletedFalse(Movie movie, Pageable pageable);

}
