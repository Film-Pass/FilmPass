package com.example.filmpass.domain.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter

public class UpdateMovieRequest {

    @Schema(description = "변경할 영화 제목", example = "변경된 영화 제목")
    String title;

    @Schema(description = "변경할 영화 표지 URL", example = "C - https://changeddomain.com")
    String url;

    @Schema(description = "변경할 영화 설명", example = "변경된 영화 설명2")
    String description;

    @Schema(description = "변경할 영화 감독", example = "변경된 영화 감독2")
    String director;

    @Schema(description = "변경할 상영 시간", example = "C - 222:34")
    String runningTime;

    @Schema(description = "변경할 영화 장르", example = "변경된 장르")
    String genre;

    public UpdateMovieRequest(String title, String director, String description, String runningTime, String url, String genre) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.director = director;
        this.runningTime = runningTime;
        this.genre = genre;
    }
}