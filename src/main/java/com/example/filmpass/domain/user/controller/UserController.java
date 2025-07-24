package com.example.filmpass.domain.user.controller;

import com.example.filmpass.domain.user.dto.UserDeleteDto;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
