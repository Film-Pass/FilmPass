package com.example.filmpass.domain.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 영화 식별자

    @Column(nullable = false)
    private String runningTime; // 상영시간 (분 단위)

    @Column(nullable = false)
    private String director; // 감독

    @Column(length = 4000)
    private String description; // 상세설명

    private String posterUrl; // 영화 표지 이미지 URL

    @Column(nullable = false)
    private String title; // 영화 이름

    public Movie(String runningTime, String director, String description, String posterUrl, String title) {
        this.runningTime = runningTime;
        this.director = director;
        this.description = description;
        this.posterUrl = posterUrl;
        this.title = title;
    }


    public void updateMovie(String title, String posterUrl, String description, String director, String runningTime) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.description = description;
        this.director = director;
        this.runningTime = runningTime;
    }
}
