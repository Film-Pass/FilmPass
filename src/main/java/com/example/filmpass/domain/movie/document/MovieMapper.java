package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MovieMapper {

    public MovieDocument toDocument(Movie movie) {
        if (movie == null) return null;

        MovieDocument doc = new MovieDocument();
        doc.setId(String.valueOf(movie.getId()));
        doc.setTitle(trimToNull(movie.getTitle()));
        doc.setDescription(trimToNull(movie.getDescription()));
        doc.setGenre(normalizeGenres(movie.getGenre()));
        doc.setAverageRating(movie.getAvrRating());
        doc.setReleaseDate(movie.getReleaseDate());
        return doc;
    }

    private String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private List<String> normalizeGenres(String csv) {
        if (csv == null) return null;
        List<String> list = Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .distinct()
                .toList(); // JDK 16+
        return list.isEmpty() ? null : list;
    }
}
