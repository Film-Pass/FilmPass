package com.example.filmpass.domain.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MovieCreateRequest {

    @Schema(description = "영화 제목", example = "영화 제목1")
    private String title;

    @Schema(description = "영화 감독", example = "감독1")
    private String director;

    @Schema(description = "영화 장르", example = "장르1")
    private String genre;

    @Schema(description = "상영 시간", example = "2:30")
    private String runningTime;

    @Schema(description = "개봉일", example = "2025.08.01")
    private String releaseDate;

    @Schema(description = "영화 설명", example = "영화 설명1")
    private String description;

    @Schema(description = "영화 표지 URL", example = "https://domain.com")
    private String posterUrl;

    public MovieCreateRequest(String title, String director, String genre, String runningTime, String releaseDate, String description, String posterUrl) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.description = description;
        this.posterUrl = posterUrl;
    }
}