package com.example.filmpass.domain.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageInfo {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;

    public PageInfo(int currentPage, int totalPages, long totalElements, int pageSize) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
    }
}