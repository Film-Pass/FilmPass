package com.example.filmpass.domain.movie.service;

import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.document.MovieMapper;
import com.example.filmpass.domain.movie.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieElasticsearchService {

    private final ElasticsearchOperations elasticTemplate;
    private final MovieMapper movieMapper;

    public void save(Movie movie) {
        if (movie == null || movie.isDelete()) return;
        MovieDocument doc = movieMapper.toDocument(movie);
        if (doc != null) elasticTemplate.save(doc);
    }

    // 통합 검색 (ko 본문 + 장르 정확매치 + 평점 가중 + 자동완성)
    public Page<MovieDocument> unifiedSearch(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return Page.empty(pageable);
        final String keyword = q.trim(); // effectively final

        //  본문 검색
        Query fullText = QueryBuilders.multiMatch(m -> m
                .query(keyword)
                .fields(Arrays.asList("title_ko^3", "overview_ko"))
                .fuzziness("AUTO")
                .lenient(true)
        );

        //  앞부분 부분일치
        Query phrasePrefix = QueryBuilders.multiMatch(m -> m
                .query(keyword)
                .fields(Arrays.asList("title_ko^3", "overview_ko"))
                .type(TextQueryType.PhrasePrefix)
                .lenient(true)
        );

        // 3) 자동완성
        Query autocomplete = QueryBuilders.multiMatch(m -> m
                .query(keyword)
                .fields(Arrays.asList("title_ko.ac^4", "overview_ko.ac"))
                .lenient(true)
        );

        // 4) 장르 매치
        Query genreExact = QueryBuilders.term(t -> t.field("genres").value(keyword));

        // 5) 조합
        Query base = QueryBuilders.bool(b -> {
            b.should(fullText);
            b.should(phrasePrefix);
            b.should(autocomplete);
            b.should(genreExact);
            b.minimumShouldMatch("1");
            return b;
        });

        // 6) 평점 가중
        Query scored = QueryBuilders.functionScore(fs -> fs
                .query(base)
                .functions(fns -> fns.fieldValueFactor(fvf -> fvf
                        .field("vote_average")
                        .factor(1.0)
                        .modifier(FieldValueFactorModifier.Sqrt)
                        .missing(0.0)
                ))
                .boostMode(FunctionBoostMode.Multiply)
                .scoreMode(FunctionScoreMode.Sum)
        );

        // 7) 페이징/정렬
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.min(Math.max(1, pageable.getPageSize()), 50);
        Pageable pageNoSort = PageRequest.of(page, size);


        // 8) 실행
        NativeQuery nq = NativeQuery.builder()
                .withQuery(scored)
                .withPageable(pageNoSort)
                .withTrackTotalHitsUpTo(10_000)
                .build();

        var hits = elasticTemplate.search(nq, MovieDocument.class);
        List<MovieDocument> items = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        return new PageImpl<>(items, pageNoSort, hits.getTotalHits());
    }
}
