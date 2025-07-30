package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class DeleteMovieResponse {
    private String movieTitle;

    public DeleteMovieResponse(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}