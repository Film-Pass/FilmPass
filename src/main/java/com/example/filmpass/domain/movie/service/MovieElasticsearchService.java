package com.example.filmpass.domain.movie.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
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
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    /** 장르 기반 추천 (정확 일치 + 부분/한글 매치) — 페이징, 정렬 없음 */
    public Page<MovieDocument> recommendByGenre(String text, Pageable pageable) {
        if (text == null || text.isBlank()) return Page.empty(pageable);

        Query query = QueryBuilders.bool(b -> b
                .should(s -> s.term(t -> t.field("genres").value(text)))          // 정확 일치 (keyword)
                .should(s -> s.match(m -> m.field("genres.text").query(text)))    // 부분/한글 매치 (text 서브필드)
                .minimumShouldMatch("1")
        );

        // 정렬 제거: page/size만 유지
        Pageable pageNoSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        NativeQuery nq = NativeQuery.builder()
                .withQuery(query)
                .withPageable(pageNoSort)
                .withTrackTotalHits(true)
                .build();

        SearchHits<MovieDocument> hits = elasticTemplate.search(nq, MovieDocument.class);
        List<MovieDocument> items = hits.getSearchHits().stream().map(SearchHit::getContent).toList();
        long total = hits.getTotalHits();
        return new PageImpl<>(items, pageNoSort, total);
    }

    /** 설명 기반 검색 (title.ko/en, overview.ko/en) — 페이징, 정렬 없음 */
    /** 설명 기반 검색 (title.ko/en, overview.ko/en) — 페이징, 정렬 없음 + 폴백(prefix/wildcard) */
    public Page<MovieDocument> recommendByDescription(String text, Pageable pageable) {
        if (text == null || text.isBlank()) return Page.empty(pageable);

        // 1) 기본: multi_match
        Query multiMatchQ = QueryBuilders.multiMatch(mmq -> mmq
                .query(text)
                .fields(Arrays.asList("title^2", "description")) // 매핑에 맞게 조정
                .fuzziness("AUTO")
        );

        // 2) 폴백 대상 필드들 (매핑에 ko/en 없으면 title/description만 두세요)
        var fallbacks = Arrays.asList("title", "description");

        // 3) prefix / wildcard 쿼리 생성
        boolean useWildcard = text.length() <= 20;

        var prefixQueries = fallbacks.stream()
                .map(f -> QueryBuilders.prefix(p -> p.field(f).value(text)))
                .toList();

        var wildcardQueries = useWildcard
                ? fallbacks.stream()
                .map(f -> QueryBuilders.wildcard(w -> w.field(f).value("*" + text + "*")))
                .toList()
                : java.util.List.<Query>of();

        // 4) should 결합 (여러 Query를 직접 추가)
        Query combined = QueryBuilders.bool(b -> {
            b.should(multiMatchQ);
            prefixQueries.forEach(b::should);
            wildcardQueries.forEach(b::should);
            b.minimumShouldMatch("1");
            return b;
        });

        Pageable pageNoSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        var nq = NativeQuery.builder()
                .withQuery(combined)
                .withPageable(pageNoSort)
                .withTrackTotalHits(true)
                .build();

        SearchHits<MovieDocument> hits = elasticTemplate.search(nq, MovieDocument.class);
        var items = hits.getSearchHits().stream().map(SearchHit::getContent).toList();
        return new PageImpl<>(items, pageNoSort, hits.getTotalHits());
    }


}
