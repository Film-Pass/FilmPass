package com.example.filmpass.domain.theater.service;


import com.example.filmpass.domain.theater.dto.PagedResponse;
import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    //극장 목록 조회
    public PagedResponse<TheaterResponse> getAllTheaters(Pageable pageable) {
        Page<Theater> page = theaterRepository.findAll(pageable);
        List<TheaterResponse> content = page.stream()
                .map(t -> new TheaterResponse(t.getId(), t.getName(), t.getLocation()))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }
}
