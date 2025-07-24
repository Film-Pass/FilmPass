package com.example.filmpass.domain.review.repository;

import com.example.filmpass.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
