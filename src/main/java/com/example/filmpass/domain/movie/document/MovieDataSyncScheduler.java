package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
//@ConditionalOnBean(ElasticsearchOperations.class)
//@ConditionalOnProperty(prefix = "app.es", name = "sync", havingValue = "true")
public class MovieDataSyncScheduler {

    private final MovieRepository movieRepository;
    private final MovieElasticsearchService movieElasticsearchService;

    // 10초마다 실행 (fixedRate)
    @Scheduled(fixedRate = 10 * 1000) // 밀리초 단위
    public void syncMoviesToES() {
        log.info("[ES Scheduled Sync] 시작");

        int total = 0;
        int success = 0;
        int failed = 0;

        for (Movie m : movieRepository.findAll()) {
            total++;
            if (m == null || m.isDelete()) continue;
            try {
                movieElasticsearchService.save(m);
                success++;
            } catch (Exception ex) {
                failed++;
                log.warn("[ES Scheduled Sync] 실패 movieId={}, cause={}",
                        Objects.requireNonNullElse(m.getId(), "null"), ex.getMessage());
            }
        }

        log.info("[ES Scheduled Sync] 완료 total={}, success={}, failed={}", total, success, failed);
    }
}
