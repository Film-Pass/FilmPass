package com.example.filmpass.domain.screen.dto;

import com.example.filmpass.domain.screen.enums.ScreenType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScreenRequestDto {

    @Schema(description = "상영관 이름", example = "상영관 이름1")
    @NotBlank(message = "상영관 이름은 필수입니다.")
    private String name;

    @Schema(description = "상영관 주소", example = "극장 1의 상영관")
    @NotBlank(message = "상영관 주소는 필수입니다.")
    private String address;

    @Schema(description = "극장 식별자", example = "1")
    @NotNull(message = "극장 ID는 필수입니다.")
    private Long theaterId;

    @Schema(description = "상영관 타입", example = "STANDARD")
    @NotNull(message = "상영관 타입은 필수입니다.")
    private ScreenType screenType;

    public ScreenRequestDto(String name, String address, ScreenType screenType, Long theaterId) {
        this.name = name;
        this.address = address;
        this.screenType = screenType;
        this.theaterId = theaterId;
    }
}
