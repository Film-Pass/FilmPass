package com.example.filmpass.domain.auth.conroller;

import com.example.filmpass.domain.auth.dto.LoginRequestDto;
import com.example.filmpass.domain.auth.dto.RoleReuqestDto;
import com.example.filmpass.domain.auth.dto.SignUpRequestDto;
import com.example.filmpass.domain.auth.service.AuthService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<ApiResponse<?>> signUp(
            @Valid @RequestBody SignUpRequestDto requestDto
    ) {

        return ResponseEntity.ok(authService.signUp(requestDto));

    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response
            ) {

        return ResponseEntity.ok(authService.login(requestDto, response));

    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<ApiResponse<?>> logout(
            @AuthenticationPrincipal UserPrincipal principal,
            HttpServletResponse response
    ) {

        Long userId = principal.getUserId();

        return ResponseEntity.ok(authService.logout(userId, response));

    }

    @PatchMapping("/api/auth/{id}")
    public ResponseEntity<ApiResponse<?>> changeRole(
            @RequestBody RoleReuqestDto request,
            @PathVariable Long id
            ) {

        return ResponseEntity.ok(authService.changeRole(request, id));

    }

}
