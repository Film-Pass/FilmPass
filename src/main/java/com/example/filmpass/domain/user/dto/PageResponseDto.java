package com.example.filmpass.domain.user.dto;

import com.example.filmpass.domain.user.entity.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponseDto {

    private Long userId;
    private String name;
    private String email;
    private String nickname;

    public PageResponseDto(Long userId, String name, String email, String nickname) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }

}
