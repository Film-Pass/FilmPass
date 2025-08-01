package com.example.filmpass.domain.auth.conroller;

import com.example.filmpass.domain.auth.dto.LoginRequestDto;
import com.example.filmpass.domain.auth.dto.RoleRequestDto;
import com.example.filmpass.domain.auth.dto.SignUpRequestDto;
import com.example.filmpass.domain.auth.service.AuthService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/api/auth/signup")
    public ApiResponse signUp(
            @Valid @RequestBody SignUpRequestDto requestDto
    ) {

        return ApiResponse.success(authService.signUp(requestDto), "회원가입 성공!");

    }

    // 로그인
    @PostMapping("/api/auth/login")
    public ApiResponse login(
            @Valid @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response
            ) {

        return ApiResponse.success(authService.login(requestDto, response), "로그인 성공!");

    }

    // 로그아웃
    @PostMapping("/api/auth/logout")
    public ApiResponse logout(
            @AuthenticationPrincipal UserPrincipal principal,
            HttpServletResponse response
    ) {

        Long userId = principal.getUserId();

        return ApiResponse.success(null, "로그아웃 성공!");

    }

    // 권한 변경
    @PatchMapping("/api/auth/{id}")
    public ApiResponse changeRole(
            @Valid @RequestBody RoleRequestDto request,
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
            ) {

        return ApiResponse.success(authService.changeRole(request, id, principal), "권한 변경 성공!");

    }

}
