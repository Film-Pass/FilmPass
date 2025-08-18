package com.example.filmpass.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {

    @Schema(description = "사용자 이름", example = "HSS")
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Schema(description = "사용자 이메일", example = "something123@domain.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(description = "비밀번호 (영문, 숫자, 특수문자 포함 8~20자)", example = "Password123!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자여야 합니다.")
    private String password;

    @Schema(description = "사용자 닉네임", example = "별명")
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

}
