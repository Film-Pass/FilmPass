package com.example.filmpass.domain.movie.repository;

import com.example.filmpass.domain.movie.dto.SearchMovieResponse;
import com.example.filmpass.domain.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
    Optional<Movie> findByDirector(String director);

    @Query(
            value = "SELECT * FROM movies " +
                    "WHERE (:id IS NULL OR id = :id) " +
                    "AND (:title IS NULL OR title = :title) " +
                    "And (:director IS NULL OR director = :director) ",

            countQuery = "SELECT COUNT(*) FROM movie " +
                    "WHERE (:id IS NULL OR id = :id) " +
                    "AND (:title IS NULL OR title = :title) " +
                    "AND (:director IS NULL OR director = :director)",

            nativeQuery = true
    )

    Page<SearchMovieResponse> searchMoviesNative(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("director") String director,
            Pageable pageable
    );
}