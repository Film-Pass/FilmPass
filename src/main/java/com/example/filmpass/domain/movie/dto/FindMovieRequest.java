package com.example.filmpass.domain.movie.dto;

import lombok.Getter;

@Getter
public class FindMovieRequest {
    private Long id;
    private String title;
    private String director;

}