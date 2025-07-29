package com.example.filmpass.domain.movie.dto;

import com.example.filmpass.domain.movie.entity.Movie;
import lombok.Getter;

@Getter
public class UpdateMovieResponse {
    private Movie movie;

    public UpdateMovieResponse(Movie movie) {
        this.movie = movie;
    }
}
