package com.example.filmpass.domain.search;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public String saveTestDoc(@RequestParam String title) throws Exception {
        return searchService.saveToElastic(title, "2025-08-03");
    }
}
