package com.example.filmpass.domain.movie.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.document.MovieMapper;
import com.example.filmpass.domain.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class MovieElasticsearchService {

    private final ElasticsearchOperations elasticTemplate;
    private final MovieMapper movieMapper;

    /** DB 엔티티 -> ES 문서 저장 */
    public void save(Movie movie) {
        if (movie == null || movie.isDelete()) return;
        MovieDocument doc = movieMapper.toDocument(movie);
        if (doc != null) elasticTemplate.save(doc);
    }

    /** 통합검색 (title/description + 장르 + 한글 폴백), 정렬 없음 */
    public Page<MovieDocument> unifiedSearch(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return Page.empty(pageable);

        // 본문 검색
        Query fullText = QueryBuilders.multiMatch(mmq -> mmq
                .query(q)
                .fields(Arrays.asList("title^2", "description"))
                .fuzziness("AUTO")
        );

        // 한글/부분 매치
        var textFields = Arrays.asList("title", "description");
        boolean useWildcard = q.length() <= 20;
        var prefixes = textFields.stream()
                .map(f -> QueryBuilders.prefix(p -> p.field(f).value(q))).toList();
        var wildcards = useWildcard
                ? textFields.stream()
                .map(f -> QueryBuilders.wildcard(w -> w.field(f).value("*" + q + "*"))).toList()
                : java.util.List.<Query>of();

        // 장르 매치
        Query genreExact = QueryBuilders.term(t -> t.field("genres").value(q));
        Query genreText  = QueryBuilders.match(m -> m.field("genres.text").query(q));

        // OR 결합
        Query combined = QueryBuilders.bool(b -> {
            b.should(fullText);
            prefixes.forEach(b::should);
            wildcards.forEach(b::should);
            b.should(genreExact);
            b.should(genreText);
            b.minimumShouldMatch("1");
            return b;
        });

        Pageable pageNoSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        var nq = NativeQuery.builder()
                .withQuery(combined)
                .withPageable(pageNoSort)
                .withTrackTotalHits(true)
                .build();

        var hits = elasticTemplate.search(nq, MovieDocument.class);
        var items = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        return new PageImpl<>(items, pageNoSort, hits.getTotalHits());
    }
}
