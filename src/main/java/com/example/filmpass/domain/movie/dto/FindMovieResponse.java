package com.example.filmpass.domain.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class FindMovieResponse<T> {
    private Long id;
    private String title;
    private String genre;
    private String rating;
    private String releaseDate;
    private PageInfo pageInFo;


    public FindMovieResponse(List<T> data, PageInfo pageInFo) {
        this.id = data.
        this.pageInFo = pageInFo;
    }
}