package com.example.filmpass.domain.screen.repository;

import com.example.filmpass.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepository extends JpaRepository<Schedule, Long> {
}
