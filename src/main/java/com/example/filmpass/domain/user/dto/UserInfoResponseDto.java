package com.example.filmpass.domain.user.dto;

import lombok.Getter;

@Getter
public class UserInfoResponseDto {

    private Long userId;
    private String name;
    private String email;
    private String nickname;

    public UserInfoResponseDto(Long userId, String name, String email, String nickname) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }

}
