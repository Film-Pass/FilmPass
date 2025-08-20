package com.example.filmpass.domain.movie.service;

import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MovieElasticsearchServiceTest {

    @Autowired
    private MovieRepository movieRepository; // JPA Repository

    @Autowired
    private MovieElasticsearchService movieElasticsearchService; // ES 서비스

    @BeforeEach
    void cleanUp() {
        movieRepository.deleteAll(); // DB 초기화
    }

    @DisplayName("DB에 저장된 영화가 ES에도 색인되고 검색된다")
    @Test
    void saveAndSearchIntegration() throws Exception {
        // given
        Movie movie = new Movie(
                "테스트 영화",
                "통합 테스트용 영화 설명",
                "감독A",
                LocalDate.of(2025, 1, 1),
                8.5
        );

        // DB에 저장하면 -> EntityListener 통해 ES 색인도 수행됨
        movieRepository.save(movie);

        // ES 색인 반영 대기 (비동기 처리 고려)
        Thread.sleep(1000);

        // when
        Page<MovieDocument> result = movieElasticsearchService
                .unifiedSearch("테스트 영화", PageRequest.of(0, 10));

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getTitleKo()).isEqualTo("테스트 영화");
    }
}
