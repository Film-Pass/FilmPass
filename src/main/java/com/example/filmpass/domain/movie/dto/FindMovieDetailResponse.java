package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class FindMovieDetailResponse {
    private String movieName;
    private String director;
    private String description;
    private String grade;
    private String review;

    public FindMovieDetailResponse(String movieName, String director, String description) {
        this.movieName = movieName;
        this.director = director;
        this.description = description;
    }
}