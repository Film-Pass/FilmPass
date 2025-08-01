package com.example.filmpass.domain.user.controller;

import com.example.filmpass.domain.user.dto.PasswordRequestDto;
import com.example.filmpass.domain.user.dto.UserInfoChangeRequestDto;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 탈퇴
    @DeleteMapping("/api/users/{id}")
    public ApiResponse deleteUser(
            @RequestBody PasswordRequestDto requestDto,
            @PathVariable Long id
            ) {

        userService.deleteUser(id, requestDto);

        return ApiResponse.success(null, "회원 탈퇴가 완료되었습니다.");

    }

    @GetMapping("/api/users")
    public ApiResponse getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal
            ) {

        return ApiResponse.success(userService.getUsers(page, size, principal), "유저목록 조회성공!");

    }

    @GetMapping("/api/users/{id}")
    public ApiResponse getUser(
        @PathVariable Long id,
        @AuthenticationPrincipal UserPrincipal principal
        ) {

        return ApiResponse.success(userService.getUser(id, principal), "유저정보 조회성공!");

    }

    @PostMapping("/api/users/me")
    public ApiResponse getMyProfile(
            @RequestBody PasswordRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return ApiResponse.success(userService.getMyProfile(requestDto ,principal), "내 정보 조회 성공!");

    }

    @PutMapping("/api/users/{id}")
    public ApiResponse changeUserInfo(
            @Valid @RequestBody UserInfoChangeRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return ApiResponse.success(userService.changeUserInfo(request, principal, id), "회원 정보가 수정되었습니다.");

    }


}
