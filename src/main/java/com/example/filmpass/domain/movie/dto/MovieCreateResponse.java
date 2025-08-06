package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class MovieCreateResponse {
    private String Title;
    private String director;
    private String genre;
    private String runningTime;

    public MovieCreateResponse(String title, String director, String genre, String runningTime) {
        Title = title;
        this.director = director;
        this.genre = genre;
        this.runningTime = runningTime;
    }
}