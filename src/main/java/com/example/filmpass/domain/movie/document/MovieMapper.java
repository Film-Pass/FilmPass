package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

    public MovieDocument toDocument(Movie movie) {
        if (movie == null) return null;

        MovieDocument doc = new MovieDocument();
        doc.setId(movie.getId());
        doc.setTitle(safe(movie.getTitle()));
        doc.setDirector(safe(movie.getDirector()));
        doc.setGenre(normalizeGenres(movie.getGenre()));
        doc.setDescription(safe(movie.getDescription()));
        doc.setAverageRating(movie.getAvrRating());

        return doc;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private List<String> normalizeGenres(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(",")).map(String::trim).filter(t -> !t.isEmpty())
                .distinct().collect(Collectors.toUnmodifiableList());
    }
}
