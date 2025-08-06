package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class MovieCreateRequest {
    private String title;
    private String director;
    private String genre;
    private String runningTime;
    private String releaseDate;
    private String description;
    private String posterUrl;

    public MovieCreateRequest(String title, String director, String genre, String runningTime, String releaseDate, String description, String posterUrl) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.description = description;
        this.posterUrl = posterUrl;
    }
}