package com.example.filmpass.domain.movie.document;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(ElasticsearchClient.class) // ES 클라이언트가 있을 때만 실행
@ConditionalOnProperty(prefix = "app.es", name = "sync", havingValue = "true", matchIfMissing = false)
public class MovieDataSyncRunner implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final ElasticsearchClient esClient;
    private final MovieMapper movieMapper;

    private static final String INDEX = "movies_v3";

    @Override
    public void run(String... args) {
        log.info("[ES] 시작");

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
                    // 단건 색인
                    var doc = movieMapper.toDocument(m);
                    if (doc != null) {
                        IndexRequest<MovieDocument> req = IndexRequest.of(i -> i
                                .index(INDEX)
                                .id(doc.getId())
                                .document(doc)
                        );
                        IndexResponse resp = esClient.index(req);
                        if (resp.result().name().equalsIgnoreCase("created") ||
                                resp.result().name().equalsIgnoreCase("updated")) {
                            success++;
                        } else {
                            failed++;
                            log.warn("[ES] 색인 결과 실패 movieId={}, result={}", doc.getId(), resp.result());
                        }
                    }
                } catch (Exception ex) {
                    failed++;
                    log.warn("[ES] 실패 movieId={}, cause={}",
                            Objects.requireNonNullElse(m.getId(), "null"), ex.getMessage());
                }
            }
            log.info("[ES] 완료 total={}, success={}, failed={}", total, success, failed);
        } catch (Exception e) {
            log.error("[ES] 실행 중 예외. 애플리케이션은 계속 동작합니다. cause={}", e.getMessage(), e);
        }
    }
}
