package com.example.filmpass.domain.movie.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.document.MovieMapper;
import com.example.filmpass.domain.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieElasticsearchService {

    private final ElasticsearchClient client;
    private final MovieMapper movieMapper;

    // 색인
    public void save(Movie movie) {
        if (movie == null || movie.isDelete()) return;
        try {
            MovieDocument doc = movieMapper.toDocument(movie);
            if (doc != null) {
                client.index(i -> i
                        .index("movies_v3")
                        .id(String.valueOf(doc.getId()))
                        .document(doc)
                );
            }
        } catch (IOException e) {
            System.err.println("ES indexing failed for movieId=" + movie.getId() + ", cause=" + e.getMessage());
        }
    }

    // 삭제
    public void delete(Long movieId) {
        if (movieId == null) return;
        try {
            client.delete(d -> d
                    .index("movies_v3")
                    .id(String.valueOf(movieId))
            );
        } catch (IOException e) {
            System.err.println("ES delete failed for movieId=" + movieId + ", cause=" + e.getMessage());
        }
    }

    // 검색
    public List<MovieDocument> unifiedSearch(String q, int page, int size) throws IOException {
        if (q == null || q.isBlank()) return List.of();
        String keyword = q.trim();

        MultiMatchQuery fullText = MultiMatchQuery.of(m -> m
                .query(keyword)
                .fields("title_ko^3", "overview_ko")
                .fuzziness("AUTO")
                .lenient(true)
        );

        MultiMatchQuery phrasePrefix = MultiMatchQuery.of(m -> m
                .query(keyword)
                .fields("title_ko^3", "overview_ko")
                .type(TextQueryType.PhrasePrefix)
                .lenient(true)
        );

        MultiMatchQuery autocomplete = MultiMatchQuery.of(m -> m
                .query(keyword)
                .fields("title_ko.ac^4", "overview_ko.ac")
                .lenient(true)
        );

        TermQuery genreExact = TermQuery.of(t -> t
                .field("genres")
                .value(keyword)
        );

        BoolQuery base = BoolQuery.of(b -> b
                .should(qb -> qb.multiMatch(fullText))
                .should(qb -> qb.multiMatch(phrasePrefix))
                .should(qb -> qb.multiMatch(autocomplete))
                .should(qb -> qb.term(genreExact))
                .minimumShouldMatch("1")
        );

        FunctionScoreQuery scored = FunctionScoreQuery.of(f -> f
                .query(qb -> qb.bool(base))
                .functions(fn -> fn.fieldValueFactor(fvf -> fvf
                        .field("vote_average")
                        .factor(1.0)
                        .modifier(FieldValueFactorModifier.Sqrt)
                        .missing(0.0)
                ))
                .scoreMode(FunctionScoreMode.Sum)
                .boostMode(FunctionBoostMode.Multiply)
        );

        SearchRequest request = SearchRequest.of(s -> s
                .index("movies_v3")
                .query(qb -> qb.functionScore(scored))
                .from(page * size)
                .size(size)
        );

        SearchResponse<MovieDocument> response = client.search(request, MovieDocument.class);
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
