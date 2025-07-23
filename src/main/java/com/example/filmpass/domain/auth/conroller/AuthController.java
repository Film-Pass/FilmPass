package com.example.filmpass.domain.auth.conroller;

import com.example.filmpass.domain.auth.dto.LoginRequestDto;
import com.example.filmpass.domain.auth.dto.SignUpRequestDto;
import com.example.filmpass.domain.auth.service.AuthService;
import com.example.filmpass.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestBody LoginRequestDto requestDto
            ) {

        return ResponseEntity.ok(authService.login(requestDto));

    }
}
