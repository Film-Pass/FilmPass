package com.example.filmpass.domain.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindMovieDetailResponse {
    private Long id;
    private String movieName;
    private String director;
    private String genre;
    private Double rating;
    private Integer reviewCount;
    private String runnigTime;
    private String releaseDate;
    private String description;
    private String url;

    public FindMovieDetailResponse(Long id, String movieName, String director, String genre, Double rating, Integer reviewCount, String runnigTime, String releaseDate, String description, String url) {
        this.id = id;
        this.movieName = movieName;
        this.director = director;
        this.genre = genre;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.runnigTime = runnigTime;
        this.releaseDate = releaseDate;
        this.description = description;
        this.url = url;
    }
}