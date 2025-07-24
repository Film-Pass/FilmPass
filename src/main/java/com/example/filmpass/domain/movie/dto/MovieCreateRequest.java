package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class MovieCreateRequest {
    private String runningTime;
    private String director;
    private String movieName;
    private String description;
    private String movieImage;


    public MovieCreateRequest(String runningTime, String director, String description, String movieImage, String movieName) {
        this.runningTime = runningTime;
        this.director = director;
        this.description = description;
        this.movieImage = movieImage;
        this.movieName = movieName;
    }
}
