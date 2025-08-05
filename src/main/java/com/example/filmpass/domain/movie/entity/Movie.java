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
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 30)
    private String director;

    @Column(length = 10)
    private String genre;

    @Column(nullable = false, name = "running_time")
    private String runningTime;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(length = 4000)
    private String description;

    @Column
    private String posterUrl;

    @Column(name = "is_delete")
    private boolean isDelete;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public Movie(String title, String director, String genre, String runningTime, String releaseDate, String description, String posterUrl) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.description = description;
        this.posterUrl = posterUrl;
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