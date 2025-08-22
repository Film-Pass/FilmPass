package com.example.filmpass.domain.movie.document;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.service.MovieElasticsearchService;
import com.example.filmpass.global.config.SpringContext;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class MovieEntityListener {

    private MovieElasticsearchService movieElasticsearchService() {
        return SpringContext.getBean(MovieElasticsearchService.class);
    }

    @PostPersist
    @PostUpdate
    public void afterSave(Movie movie) {
        if (!movie.isDelete()) {
            movieElasticsearchService().save(movie);
        } else {
            movieElasticsearchService().delete(movie.getId());
        }
    }

    @PostRemove
    public void afterDelete(Movie movie) {
        movieElasticsearchService().delete(movie.getId());
    }
}