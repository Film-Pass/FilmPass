package com.example.filmpass.domain.movie.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDocument {

    @Id
    private Long id;
    private String title;
    private String description;
    private String director;
    private List<String> genre;
    private Double averageRating;
}
