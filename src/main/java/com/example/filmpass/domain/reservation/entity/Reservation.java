package com.example.filmpass.domain.reservation.entity;

import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.seat.entity.Seat;
import com.example.filmpass.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime reservationAt;

    private boolean softDeleted;

    public Reservation(Schedule schedule, Seat seat, User user) {
        this.schedule = schedule;
        this.seat = seat;
        this.user = user;
        this.reservationAt = LocalDateTime.now();
        this.softDeleted = false;
    }
}
