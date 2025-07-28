package com.example.filmpass.domain.user.controller;

import com.example.filmpass.domain.user.dto.PasswordRequestDto;
import com.example.filmpass.domain.user.dto.UserInfoChangeRequestDto;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @RequestBody PasswordRequestDto requestDto,
            @PathVariable Long id
            ) {

        return ResponseEntity.ok(userService.deleteUser(id, requestDto));

    }

    @GetMapping("/api/users")
    public ResponseEntity<ApiResponse<?>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal
            ) {

        return ResponseEntity.ok(userService.getUsers(page, size, principal));

    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(userService.getUser(id, principal));

    }

    @PostMapping("/api/users/me")
    public ResponseEntity<ApiResponse<?>> getMyProfile(
            @RequestBody PasswordRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ResponseEntity.ok(userService.getMyProfile(requestDto ,principal));

    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<?>> changeUserInfo(
            @RequestBody UserInfoChangeRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.changeUserInfo(request, principal, id));

    }


}
