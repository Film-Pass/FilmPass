package com.example.filmpass.domain.theater.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterRequest {
    @NotBlank(message = "극장 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "위치는 필수입니다.")
    private String location;
}