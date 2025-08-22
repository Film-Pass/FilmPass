package com.example.filmpass.domain.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindMovieResponse<T> {
    private List<T> Movie;
    private PageInfo pageInFo;


    public FindMovieResponse(List<T> data, PageInfo pageInFo) {
        this.Movie = data;
        this.pageInFo = pageInFo;
    }
}