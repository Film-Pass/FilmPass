package com.example.filmpass.domain.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TheaterResponse {
    private Long id;
    private String name;
    private String location;
}
