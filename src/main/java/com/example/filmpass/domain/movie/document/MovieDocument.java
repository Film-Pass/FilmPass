package com.example.filmpass.domain.movie.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "movies_v2")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MovieDocument {

    @Id
    private Long id;

    @Field(name = "title_ko", type = FieldType.Text)
    @JsonProperty("title_ko")
    private String title;

    @Field(name = "overview_ko", type = FieldType.Text)
    @JsonProperty("overview_ko")
    private String description;

    @Field(name = "genres", type = FieldType.Keyword)
    @JsonProperty("genres")
    private List<String> genre;

    @Field(name = "vote_average", type = FieldType.Float)
    @JsonProperty("vote_average")
    private Double averageRating;

    @Field(name = "release_date", type = FieldType.Date)
    @JsonProperty("release_date")
    private LocalDate releaseDate;

    // ES 문서에 director가 아직 없으니 값이 필요하면 색인 쪽에서도 넣어주세요.
    private String director;
}
