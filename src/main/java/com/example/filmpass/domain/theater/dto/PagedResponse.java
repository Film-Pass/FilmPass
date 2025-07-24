package com.example.filmpass.domain.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;        // 데이터 리스트
    private int pageNumber;         // 현재 페이지 번호 (0부터 시작)
    private int pageSize;           // 한 페이지 사이즈
    private int totalPages;         // 전체 페이지 수
    private long totalElements;     // 전체 데이터 수
    private boolean last;           // 마지막 페이지 여부
}