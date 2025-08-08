package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieDataSyncRunner implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final MovieElasticsearchService movieElasticsearchService;

    @Override
    public void run(String... args) {movieRepository.findAll().forEach(movieElasticsearchService::save);
    }
}
