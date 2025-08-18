package com.example.filmpass.domain.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class FindMovieRequest {

    @Schema(description = "영화 식별자", example = "1")
    private Long id;

    @Schema(description = "영화 제목", example = "null")
    private String title;

    @Schema(description = "영화 감독", example = "null")
    private String director;

    @Schema(description = "영화 장르", example = "null")
    private String genre;

    public FindMovieRequest(Long id, String title, String director, String genre) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.genre = genre;
    }
}