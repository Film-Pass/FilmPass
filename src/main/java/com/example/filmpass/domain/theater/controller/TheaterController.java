package com.example.filmpass.domain.theater.controller;

import com.example.filmpass.domain.theater.dto.TheaterRequest;
import com.example.filmpass.domain.theater.dto.TheaterResponse;
import com.example.filmpass.domain.theater.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    // 극장 등록 어도민 권한으로만 가능하게
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterResponse> createTheater(@RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.createTheater(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
