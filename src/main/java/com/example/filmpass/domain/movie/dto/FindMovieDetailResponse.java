package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class FindMovieDetailResponse {
    private String movieName;
    private String director;
    private String description;
    private String runnigTime;
    private String url;
    private String rating;
    private String review;
    private String genre;

    public FindMovieDetailResponse(String movieName, String director, String description, String genre, String rating, String runnigTime, String url) {
        this.movieName = movieName;
        this.director = director;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.runnigTime = runnigTime;
        this.url = url;
    }
}