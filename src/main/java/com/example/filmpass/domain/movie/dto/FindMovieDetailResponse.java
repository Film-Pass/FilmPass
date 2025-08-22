package com.example.filmpass.domain.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindMovieDetailResponse {
    private Long id;
    private String title;
    private String director;
    private String genre;
    private Double avrRating;
    private Integer reviewCount;
    private String runnigTime;
    private String releaseDate;
    private String description;
    private String posterUrl;

    public FindMovieDetailResponse(Long id, String title, String director, String genre, Double avrRating, Integer reviewCount, String runnigTime, String releaseDate, String description, String posterUrl) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.avrRating = avrRating;
        this.reviewCount = reviewCount;
        this.runnigTime = runnigTime;
        this.releaseDate = releaseDate;
        this.description = description;
        this.posterUrl = posterUrl;
    }
}