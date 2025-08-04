package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class FindMovieDetailResponse {
    private String movieName;
    private String director;
    private String description;
    private String grade;
    private String review;
    private String genre;

    public FindMovieDetailResponse(String movieName, String director, String description, String genre) {
        this.movieName = movieName;
        this.director = director;
        this.description = description;
        this.genre = genre;
    }
}