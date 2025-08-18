package com.example.filmpass.domain.theater.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterRequest {

    @Schema(description = "극장 이름", example = "극장1")
    @NotBlank(message = "극장 이름은 필수입니다.")
    private String name;

    @Schema(description = "극장 주소", example = "주소1")
    @NotBlank(message = "위치는 필수입니다.")
    private String location;
}