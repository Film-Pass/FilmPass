package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter

public class UpdateMovieRequest {
    String title;
    String url;
    String description;
    String director;
    String runningTime;

    public UpdateMovieRequest(String title, String url, String description, String director, String runningTime) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.director = director;
        this.runningTime = runningTime;
    }
}
