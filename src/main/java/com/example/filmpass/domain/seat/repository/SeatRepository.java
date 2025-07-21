package com.example.filmpass.domain.seat.repository;

import com.example.filmpass.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
