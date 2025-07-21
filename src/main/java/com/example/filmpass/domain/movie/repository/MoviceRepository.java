package com.example.filmpass.domain.movie.repository;

import com.example.filmpass.domain.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviceRepository extends JpaRepository<Movie, Long> {
}
