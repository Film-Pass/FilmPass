package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(ElasticsearchOperations.class)
@ConditionalOnProperty(prefix = "app.es", name = "bootstrap", havingValue = "true", matchIfMissing = false)
public class EsBootstrapRunner implements CommandLineRunner {

    private final ElasticsearchOperations es;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ResourceLoader resourceLoader;

    private static final String INDEX = "movies_v2";

    @Override
    public void run(String... args) {
        try {
            IndexCoordinates idx = IndexCoordinates.of(INDEX);IndexOperations ops = es.indexOps(idx);

            // 1) 인덱스 생성 (settings/mappings)
            if (!ops.exists()) {
                log.info("Creating index {} ...", INDEX);
                Document settings = loadJsonAsDocument("classpath:es/movies_v2-settings.json");
                Document mappings = loadJsonAsDocument("classpath:es/movies_v2-mappings.json");
                ops.create(settings);
                ops.putMapping(mappings);
                log.info("Index {} created.", INDEX);
            } else {
                log.info("Index {} already exists. Skipping creation.", INDEX);
            }

            // 2) DB → ES 벌크 색인
            List<Movie> movies = movieRepository.findAll();
            var docs = movies.stream()
                    .filter(Objects::nonNull)
                    .filter(m -> !m.isDelete())
                    .map(movieMapper::toDocument)
                    .toList();

            if (!docs.isEmpty()) {
                es.save(docs, idx); // bulk 저장
                log.info("Indexed {} movies into {}.", docs.size(), INDEX);
            } else {
                log.info("No movies to index (empty or all deleted).");
            }
        } catch (Exception e) {
            log.error("ES bootstrap failed; application will continue. cause={}", e.getMessage(), e);
        }
    }

    private Document loadJsonAsDocument(String location) throws IOException {
        Resource res = resourceLoader.getResource(location);
        String json = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return Document.parse(json);
    }
}
