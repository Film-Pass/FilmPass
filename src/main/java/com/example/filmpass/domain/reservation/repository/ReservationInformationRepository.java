package com.example.filmpass.domain.reservation.repository;

import com.example.filmpass.domain.reservation.entity.ReservationInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationInformationRepository extends JpaRepository<ReservationInformation, Long> {
}
