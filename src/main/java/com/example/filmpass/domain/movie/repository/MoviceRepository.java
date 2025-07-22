package com.example.filmpass.domain.movie.repository;

import com.example.filmpass.domain.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviceRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
    Optional<Movie> findByDirector(String director);

    List<Movie> id(Long id);
}
