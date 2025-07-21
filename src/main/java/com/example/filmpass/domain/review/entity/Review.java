package com.example.filmpass.domain.review.entity;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰 아이디

    @Column(nullable = false)
    private int rating; // 평점

    @Column(nullable = false, length = 1000)
    private String content; // 리뷰 내용


    private Boolean deleted = false; // 삭제 여부 (soft delete)
    private LocalDateTime deletedAt; // 삭제일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie; // 영화 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 아이디
}
