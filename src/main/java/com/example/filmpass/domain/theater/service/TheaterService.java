package com.example.filmpass.domain.theater.service;

import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.entity.Theater;
import com.example.filmpass.domain.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    // 극장 목록 조회
    public List<TheaterResponse> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(theater -> new TheaterResponse(
                        theater.getId(),
                        theater.getName(),
                        theater.getLocation() // 필드명 확인 필요
                ))
                .collect(Collectors.toList());
    }
}
