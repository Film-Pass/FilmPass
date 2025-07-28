package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class MovieCreateResponse {
    private String Title;

    public MovieCreateResponse(String title) {
        Title = title;
    }
}
