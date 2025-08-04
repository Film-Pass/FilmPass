package com.example.filmpass.domain.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@SQLDelete(sql =
        "UPDATE movies " +
                "SET is_delete = true, deleted_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?")
@Where(clause = "is_delete = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 영화 식별자

    @Column(nullable = false)
    private String title; // 영화 이름

    @Column(nullable = false)
    private String director; // 감독

    @Column(length = 4000)
    private String description; // 상세설명

    @Column(nullable = false)
    private String runningTime; // 상영시간 (?시간 ?분)

    @Column
    private String posterUrl; // 영화 표지 이미지 URL

    @Column
    private String genre;

    @Column
    private boolean isDelete;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public Movie(String title, String director, String description, String runningTime, String posterUrl, String genre) {
        this.runningTime = runningTime;
        this.director = director;
        this.description = description;
        this.posterUrl = posterUrl;
        this.title = title;
        this.genre = genre;
    }


    public void updateMovie(String title, String posterUrl, String description, String director, String runningTime, String genre) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.description = description;
        this.director = director;
        this.runningTime = runningTime;
        this.genre = genre;
    }

    public void deleteMovie () {
        this.isDelete = true;
        this.deletedAt = LocalDateTime.now();
    }
}