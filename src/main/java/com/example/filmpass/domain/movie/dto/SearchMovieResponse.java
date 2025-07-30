package com.example.filmpass.domain.movie.dto;

import com.example.filmpass.domain.movie.entity.Movie;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchMovieResponse {
    private Movie movie;

    public SearchMovieResponse(Movie movie) {
        this.movie = movie;
    }
}