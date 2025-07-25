package com.example.filmpass.domain.user.dto;

import com.example.filmpass.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDetailsResponseDto {

    private Long userId;
    private String name;
    private String email;
    private String nickname;
    private LocalDateTime deletedAt;
    private String userRole;


    public UserDetailsResponseDto(Long userId, String name, String email, String nickname, LocalDateTime deletedAt, String userRole) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.deletedAt = deletedAt;
        this.userRole = userRole;
    }
}
