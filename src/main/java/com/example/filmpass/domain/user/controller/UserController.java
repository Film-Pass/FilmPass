package com.example.filmpass.domain.user.controller;

import com.example.filmpass.domain.user.dto.PasswordRequestDto;
import com.example.filmpass.domain.user.dto.UserInfoChangeRequestDto;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.aop.TrackUserActionAnnotation;
import com.example.filmpass.global.common.CommonResponse;
import com.example.filmpass.global.config.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴시킵니다.")
    public CommonResponse deleteUser(
            @Parameter(name = "password", description = "비밀번호", required = true, example = "Qwer1234!")
            @RequestHeader("password") String password,
            @PathVariable Long id
            ) {

        userService.deleteUser(id, password);

        return CommonResponse.success(null, "회원 탈퇴가 완료되었습니다.");

    }

    // 유저 목록 조회
    @GetMapping("/api/users")
    @Operation(summary = "(관리자 전용) 회원 목록 조회", description = "회원목록과 간단한 정보를 조회합니다.")
    @TrackUserActionAnnotation("유저 목록 조회")
    public CommonResponse getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal
            ) {

        return CommonResponse.success(userService.getUsers(page, size, principal), "유저목록 조회성공!");

    }

    // 유저 단건 조회
    @GetMapping("/api/users/{id}")
    @Operation(summary = "(관리자 전용) 회원 상세정보 조회", description = "회원의 상제정보를 조회합니다.")
    @TrackUserActionAnnotation("유저 단건 조회")
    public CommonResponse getUser(
        @PathVariable Long id,
        @AuthenticationPrincipal UserPrincipal principal
        ) {

        return CommonResponse.success(userService.getUser(id, principal), "유저정보 조회성공!");

    }

    // 내 정보 조회
    @PostMapping("/api/users/me")
    @Operation(summary = "내 정보 조회", description = "로그인한 유저의 정보를 조회합니다.")
    public CommonResponse getMyProfile(
            @RequestBody PasswordRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return CommonResponse.success(userService.getMyProfile(requestDto ,principal), "내 정보 조회 성공!");

    }

    // 내 정보 수정
    @PutMapping("/api/users/{id}")
    @Operation(summary = "내 정보 수정", description = "로그인한 유저의 정보를 수정합니다.")
    public CommonResponse changeUserInfo(
            @Valid @RequestBody UserInfoChangeRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        return CommonResponse.success(userService.changeUserInfo(request, principal, id), "회원 정보가 수정되었습니다.");

    }



}
