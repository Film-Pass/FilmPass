package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EsBootstrapRunner implements CommandLineRunner {

    private final ElasticsearchOperations es;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ResourceLoader resourceLoader;

    private static final String INDEX = "movies_v3";

    @Override
    public void run(String... args) throws Exception {
        IndexCoordinates idx = IndexCoordinates.of(INDEX);
        IndexOperations ops = es.indexOps(idx);

        // 1) 인덱스 없으면 settings/mappings로 생성
        if (!ops.exists()) {
            log.info("Creating index {} ...", INDEX);

            Document settings = loadJsonAsDocument("classpath:es/movies_v3-settings.json");
            Document mappings = loadJsonAsDocument("classpath:es/movies_v3-mappings.json");

            ops.create(settings);          // settings 적용
            ops.putMapping(mappings);      // mappings 적용

            log.info("Index {} created.", INDEX);
        } else {
            log.info("Index {} already exists. Skipping creation.", INDEX);
        }

        // 2) DB → ES 벌크 색인
        List<Movie> movies = movieRepository.findAll();
        var docs = movies.stream()
                .filter(m -> m != null && !m.isDelete())
                .map(movieMapper::toDocument)
                .toList();

        if (!docs.isEmpty()) {
            es.save(docs, idx); // bulk 저장
            log.info("Indexed {} movies into {}.", docs.size(), INDEX);
        } else {
            log.info("No movies to index (empty or all deleted).");
        }
    }

    private Document loadJsonAsDocument(String location) throws Exception {
        Resource res = resourceLoader.getResource(location);
        String json = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return Document.parse(json);
    }
}
