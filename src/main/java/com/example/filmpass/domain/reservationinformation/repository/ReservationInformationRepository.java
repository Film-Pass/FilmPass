package com.example.filmpass.domain.reservationinformation.repository;

import com.example.filmpass.domain.reservationinformation.entity.ReservationInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationInformationRepository extends JpaRepository<ReservationInformation, Long> {
}
