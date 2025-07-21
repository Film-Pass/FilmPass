package com.example.filmpass.domain.screen.entity;

import com.example.filmpass.domain.theater.entity.Theater;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 상영관 식별자

    @Column(nullable = false)
    private String name; // 상영관 이름 (예: 1관, IMAX관)

    @Column(nullable = false)
    private String address; // 상영관 주소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater; // 극장 식별자 (FK)
}
