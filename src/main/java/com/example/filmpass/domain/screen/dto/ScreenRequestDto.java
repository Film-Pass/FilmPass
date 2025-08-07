package com.example.filmpass.domain.screen.dto;

import com.example.filmpass.domain.screen.enums.ScreenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScreenRequestDto {

    @NotBlank(message = "상영관 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "상영관 주소는 필수입니다.")
    private String address;

    @NotNull(message = "극장 ID는 필수입니다.")
    private Long theaterId;

    @NotNull(message = "상영관 타입은 필수입니다.")
    private ScreenType screenType;

    public ScreenRequestDto(String name, String address, ScreenType screenType, Long theaterId) {
        this.name = name;
        this.address = address;
        this.screenType = screenType;
        this.theaterId = theaterId;
    }
}
