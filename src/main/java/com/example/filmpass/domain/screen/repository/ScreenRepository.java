package com.example.filmpass.domain.screen.repository;

import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.theater.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    boolean existsByNameAndTheaterId(String name, Long theaterId);

    boolean existsByNameAndTheaterIdAndIdNot(String name, Long theaterId, Long id);

    List<Screen> findByTheater(Theater theater);

}
