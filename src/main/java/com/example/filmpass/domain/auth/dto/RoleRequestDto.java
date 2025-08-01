package com.example.filmpass.domain.auth.dto;

import com.example.filmpass.domain.user.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RoleRequestDto {

    @NotNull(message = "권한을 입력해주세요.")
    private UserRole userRole;

}
