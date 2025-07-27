package com.example.filmpass.domain.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class FindMovieResponse<T> {
    private List<T> data;
    private PageInfo pageInFo;

    public FindMovieResponse(List<T> data, PageInfo pageInFo) {
        this.data = data;
        this.pageInFo = pageInFo;
    }
}
