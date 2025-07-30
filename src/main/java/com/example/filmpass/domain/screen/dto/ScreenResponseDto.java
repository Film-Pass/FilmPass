package com.example.filmpass.domain.screen.dto;

import com.example.filmpass.domain.screen.entity.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenResponseDto {
    private Long id;
    private String name;
    private String address;
    private Long theaterId;

    public static ScreenResponseDto from(Screen screen) {
        return new ScreenResponseDto(
                screen.getId(),
                screen.getName(),
                screen.getAddress(),
                screen.getTheater().getId()
        );
    }
}
