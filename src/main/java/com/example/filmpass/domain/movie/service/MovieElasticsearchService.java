package com.example.filmpass.domain.movie.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.document.MovieMapper;
import com.example.filmpass.domain.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieElasticsearchService {

    private final ElasticsearchOperations elasticTemplate;
    private final MovieMapper movieMapper;

    /** ES에 저장 (삭제된 영화는 제외) */
    public void save(Movie movie) {
        if (movie == null || movie.isDelete()) return;
        MovieDocument doc = movieMapper.toDocument(movie);
        if (doc != null) elasticTemplate.save(doc);
    }

    /** 장르 기반 추천 (영문/한글 모두 검색 가능) */
    public List<MovieDocument> recommendByGenre(String text) {
        if (text == null || text.isBlank()) return List.of();

        Query query = QueryBuilders.bool(b -> b
                .should(s -> s.term(t -> t.field("genre").value(text)))
                .should(s -> s.match(m -> m.field("genre.ko").query(text)))
                .minimumShouldMatch("1")
        );

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(0, 10))
                .build();

        return elasticTemplate.search(searchQuery, MovieDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    /** 설명 기반 검색 (title, description, descriptionKo) */
    public List<MovieDocument> recommendByDescription(String text) {
        if (text == null || text.isBlank()) return List.of();

        Query query = QueryBuilders.multiMatch(mm -> mm
                .query(text)
                .fields("title^2", "description", "descriptionKo")
                .fuzziness("AUTO")
        );

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(0, 10))
                .build();

        return elasticTemplate.search(searchQuery, MovieDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
