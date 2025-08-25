package com.example.filmpass.domain.seat.repository;

import com.example.filmpass.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    boolean existsByScreenIdAndSeatNumber(Long id, String seatNumber);
    List<Seat> findByScreenId(Long screenId);
}
