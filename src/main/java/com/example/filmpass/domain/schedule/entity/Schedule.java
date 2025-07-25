package com.example.filmpass.domain.schedule.entity;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.screen.entity.Screen;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public Schedule(LocalDateTime startAt, LocalDateTime endAt, Screen screen, Movie movie) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.screen = screen;
        this.movie = movie;
    }
    public void update(LocalDateTime startAt, LocalDateTime endAt, Screen screen, Movie movie) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.screen = screen;
        this.movie = movie;
    }
}
