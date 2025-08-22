package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class MovieSearchController {

    private final MovieElasticsearchService service;

    @GetMapping("/elastic")
    public Object unified(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws IOException {
        // 기존 unifiedSearch 메서드에 맞게 int, int 전달
        return service.unifiedSearch(keyword, page, size);
    }
}
