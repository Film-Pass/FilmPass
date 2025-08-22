package com.example.filmpass.domain.schedule.repository;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.schedule.entity.Schedule;
import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.theater.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.screen.id = :screenId AND " +
            "(:startAt < s.endAt AND :endAt > s.startAt)")
    List<Schedule> findOverlappingSchedules(@Param("screenId") Long screenId,
                                            @Param("startAt") LocalDateTime startAt,
                                            @Param("endAt") LocalDateTime endAt);

    List<Schedule> findByScreenInAndMovieAndStartAtAfter(List<Screen> screens, Movie movie, LocalDateTime now);

}
