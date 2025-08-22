package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class DeleteMovieResponse {
    private String Title;

    public DeleteMovieResponse(String Title) {
        this.Title = Title;
    }
}