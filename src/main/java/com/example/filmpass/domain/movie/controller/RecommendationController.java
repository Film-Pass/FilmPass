package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final MovieElasticsearchService movieElasticsearchService;

    @GetMapping("/genre")
    public List<MovieDocument> recommendByGenre(@RequestParam String genre) {
        return movieElasticsearchService.recommendByGenre(genre);
    }

    @GetMapping("/description")
    public List<MovieDocument> recommendByDescription(@RequestParam String text) {
        return movieElasticsearchService.recommendByDescription(text);
    }
}
