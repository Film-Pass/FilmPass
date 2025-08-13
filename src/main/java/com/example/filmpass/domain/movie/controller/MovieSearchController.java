package com.example.filmpass.domain.movie.controller;

import com.example.filmpass.domain.movie.document.MovieDocument;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class MovieSearchController {

    private final MovieElasticsearchService service;

    @GetMapping("/elastic")
    public Page<MovieDocument> unified(
            @RequestParam("q") String q,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size
    ) {
        return service.unifiedSearch(q, PageRequest.of(page, size));
    }
}