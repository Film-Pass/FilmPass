package com.example.filmpass.domain.reservation.entity;

import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_information")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예약정보 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule; // 스케줄 아이디 (연관관계)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 아이디 (연관관계)

    @Column(nullable = false)
    private LocalDateTime reservationDate; // 예약 날짜

    private LocalDateTime createdAt; // 생성일
}
