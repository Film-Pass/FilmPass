package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final MovieElasticsearchService movieElasticsearchService;

    // 장르 기반 추천 (page/size만, sort 없음)
    @GetMapping("/genre")
    public Page<MovieDocument> recommendByGenre(
            @RequestParam String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size); // ← 정렬 제거
        return movieElasticsearchService.recommendByGenre(genre, pageable);
    }

    // 설명 기반 추천 (page/size만, sort 없음)
    @GetMapping("/description")
    public Page<MovieDocument> recommendByDescription(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size); // ← 정렬 제거
        return movieElasticsearchService.recommendByDescription(text, pageable);
    }
}
