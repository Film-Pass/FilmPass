package com.example.filmpass.domain.review.entity;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId; // 리뷰 ID

    @Column(nullable = false)
    private Integer rating; // 평점

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 리뷰 내용

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 작성일

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정일

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted; // 삭제 여부

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 삭제일시

    // movie_id 외래키, Movie 엔티티 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // user_id 외래키, User 엔티티 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 생성자
    public Review(Integer rating, String content, Movie movie, User user) {
        this.rating = rating;
        this.content = content;
        this.movie = movie;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
