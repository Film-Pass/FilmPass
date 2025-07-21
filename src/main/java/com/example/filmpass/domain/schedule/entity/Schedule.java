package com.example.filmpass.domain.schedule.entity;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.screen.entity.Screen;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 스케줄 아이디

    @Column(nullable = false)
    private LocalDateTime startTime; // 시작 시간

    @Column(nullable = false)
    private LocalDateTime endTime;   // 종료 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen; // 상영관 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;   // 영화 식별자

}

