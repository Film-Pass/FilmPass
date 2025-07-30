package com.example.filmpass.domain.auth.dto;

import com.example.filmpass.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class RoleRequestDto {

    private UserRole userRole;

}
