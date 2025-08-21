package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(ElasticsearchClient.class)
@ConditionalOnProperty(prefix = "app.es", name = "bootstrap", havingValue = "true", matchIfMissing = false)
public class EsBootstrapRunner implements CommandLineRunner {

    private final ElasticsearchClient client;
    private final MovieRepository movieRepository;
    private final MovieElasticsearchService movieElasticsearchService;
    private final ResourceLoader resourceLoader;

    private static final String INDEX = "movies_v2";

    @Override
    public void run(String... args) {
        try {
            // 1) 인덱스 생성
            if (!client.indices().exists(b -> b.index(INDEX)).value()) {
                log.info("Creating index {} ...", INDEX);

                // settings
                Resource settingsRes = resourceLoader.getResource("classpath:es/movies_v2-settings.json");
                try (InputStream is = settingsRes.getInputStream()) {
                    client.indices().create(CreateIndexRequest.of(b -> b
                            .index(INDEX)
                            .withJson(is)
                    ));
                }

                // mappings
                Resource mappingsRes = resourceLoader.getResource("classpath:es/movies_v2-mappings.json");
                try (InputStream is = mappingsRes.getInputStream()) {
                    client.indices().putMapping(PutMappingRequest.of(b -> b
                            .index(INDEX)
                            .withJson(is)
                    ));
                }

                log.info("Index {} created.", INDEX);
            } else {
                log.info("Index {} already exists. Skipping creation.", INDEX);
            }

            // 2) DB → ES 색인
            List<Movie> movies = movieRepository.findAll();
            for (Movie m : movies) {
                if (m != null && !m.isDelete()) {
                    try {
                        movieElasticsearchService.save(m);
                    } catch (Exception ex) {
                        log.warn("Failed indexing movieId={}, cause={}",
                                Objects.requireNonNullElse(m.getId(), "null"), ex.getMessage());
                    }
                }
            }

            log.info("ES bootstrap completed, total movies indexed: {}", movies.size());

        } catch (Exception e) {
            log.error("ES bootstrap failed; application will continue. cause={}", e.getMessage(), e);
        }
    }
}
