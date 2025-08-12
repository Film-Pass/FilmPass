package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(ElasticsearchOperations.class) // ES 클라이언트가 있을 때만 실행
@ConditionalOnProperty(prefix = "app.es", name = "sync", havingValue = "true", matchIfMissing = false) // 토글
public class MovieDataSyncRunner implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final MovieElasticsearchService movieElasticsearchService;

    @Override
    public void run(String... args) {
        log.info("[ES Sync] 시작");

        int total = 0;
        int success = 0;
        int failed = 0;

        try {
            for (Movie m : movieRepository.findAll()) {
                total++;
                if (m == null || m.isDelete()) {
                    continue;
                }
                try {
                    movieElasticsearchService.save(m); // 기존 단건 저장 로직 사용
                    success++;
                } catch (Exception ex) {
                    failed++;
                    // 개별 실패는 기록만 하고 계속 진행 (앱 종료 X)
                    log.warn("[ES Sync] 실패 movieId={}, cause={}",
                            Objects.requireNonNullElse(m.getId(), "null"), ex.getMessage());
                }
            }
            log.info("[ES Sync] 완료 total={}, success={}, failed={}", total, success, failed);
        } catch (Exception e) {
            // 최상위 예외도 삼켜서 앱이 내려가지 않게
            log.error("[ES Sync] 실행 중 예외. 애플리케이션은 계속 동작합니다. cause={}", e.getMessage(), e);
        }
    }
}
