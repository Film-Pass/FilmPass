package com.example.filmpass.domain.reservation.repository;

import com.example.filmpass.domain.reservation.entity.Reservation;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.seat.entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByScheduleAndSeat(Schedule schedule, Seat seat);

    Page<Reservation> findByUserId(Long userId, Pageable pageable);
}
