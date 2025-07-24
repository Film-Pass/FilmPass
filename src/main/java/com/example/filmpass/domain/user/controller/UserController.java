package com.example.filmpass.domain.user.controller;

import com.example.filmpass.domain.user.dto.UserDeleteDto;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @RequestBody UserDeleteDto requestDto,
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
    public ResponseEntity<ApiResponse<?>> getUser(
        @PathVariable Long id,
        @AuthenticationPrincipal UserPrincipal principal
        ) {

        return ResponseEntity.ok(userService.getUser(id, principal));

    }


}
