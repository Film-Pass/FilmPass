package com.example.filmpass.domain.movie.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDocument {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title_ko")
    private String title;

    @JsonProperty("overview_ko")
    private String description;

    @JsonProperty("genres")
    private List<String> genre;

    @JsonProperty("vote_average")
    private Double averageRating;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("director")
    private String director;
}
