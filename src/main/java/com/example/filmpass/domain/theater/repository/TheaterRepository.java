package com.example.filmpass.domain.theater.repository;

import com.example.filmpass.domain.theater.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
