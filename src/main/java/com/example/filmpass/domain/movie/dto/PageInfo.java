package com.example.filmpass.domain.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PageInfo {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean isLast;

    public PageInfo(int currentPage, int totalPages, long totalElements, int pageSize, boolean isLast) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.isLast = isLast;
    }
}
