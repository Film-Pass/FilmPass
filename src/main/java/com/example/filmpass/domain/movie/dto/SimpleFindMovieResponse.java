package com.example.filmpass.domain.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleFindMovieResponse {
    private Long id;
    private String title;
    private String genre;
    private Double rating;
    private String releaseDate;

    public SimpleFindMovieResponse(Long id, String title, String genre, Double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }
}
