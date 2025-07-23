package com.example.filmpass.domain.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TheaterRequest {
    private String name;
    private String location;
}
