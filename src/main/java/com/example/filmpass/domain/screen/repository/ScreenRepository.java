package com.example.filmpass.domain.screen.repository;

import com.example.filmpass.domain.screen.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    boolean existsByNameAndTheaterId(String name, Long theaterId);

}
