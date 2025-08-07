package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter

public class UpdateMovieRequest {
    String title;
    String url;
    String description;
    String director;
    String runningTime;
    String genre;

    public UpdateMovieRequest(String title, String director, String description, String runningTime, String url, String genre) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.director = director;
        this.runningTime = runningTime;
        this.genre = genre;
    }
}