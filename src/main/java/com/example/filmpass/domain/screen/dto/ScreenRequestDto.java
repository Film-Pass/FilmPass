package com.example.filmpass.domain.screen.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Valid
public class ScreenRequestDto {

    @NotBlank(message = "상영관 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "상영관 주소는 필수입니다.")
    private String address;

    @NotNull(message = "극장 ID는 필수입니다.")
    private Long theaterId;
}
