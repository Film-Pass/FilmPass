package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class FindMovieRequest {
    private Long id;
    private String title;
    private String director;
    private String genre;

    public FindMovieRequest(Long id, String title, String director, String genre) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.genre = genre;
    }
}