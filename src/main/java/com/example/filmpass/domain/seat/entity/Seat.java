package com.example.filmpass.domain.seat.entity;

import com.example.filmpass.domain.screen.entity.Screen;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 좌석 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen; // 상영관 (Screen) 식별자 - 연관관계

    @Column(nullable = false)
    private String seat_Number; // 좌석 이름 (예: A1, B2)
}
