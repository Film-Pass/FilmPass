package com.example.filmpass.domain.movie.dto;

import com.example.filmpass.domain.movie.entity.Movie;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchMovieResponse {
    private List<Movie> movieList;
    private PageInfo pageInfo;

    public SearchMovieResponse(List<Movie> movieList, PageInfo pageInfo) {
        this.movieList = movieList;
        this.pageInfo = pageInfo;
    }
}
