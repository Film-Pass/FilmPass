package com.example.filmpass.domain.auth.conroller;

import com.example.filmpass.domain.auth.dto.DiscountTypeRequestDto;
import com.example.filmpass.domain.auth.dto.LoginRequestDto;
import com.example.filmpass.domain.auth.dto.RoleRequestDto;
import com.example.filmpass.domain.auth.dto.SignUpRequestDto;
import com.example.filmpass.domain.auth.service.AuthService;
import com.example.filmpass.global.aop.TrackUserActionAnnotation;
import com.example.filmpass.global.common.CommonResponse;
import com.example.filmpass.global.common.swagger.domain.auth.SignUpError;
import com.example.filmpass.global.common.swagger.domain.auth.SignUpSuccess;
import com.example.filmpass.global.config.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    @Operation(
            summary = "회원가입",
            description = "입력받은 사용자 정보를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignUpSuccess.class))),
                    @ApiResponse(responseCode = "400", description = "이메일 혹은 닉네임 중복", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignUpError.class)))
            }
    )
    public CommonResponse signUp(@Valid @RequestBody SignUpRequestDto requestDto) {

        return CommonResponse.success(authService.signUp(requestDto), "회원가입 성공!");

    }

    // 로그인
    @PostMapping("/api/auth/login")
    @Operation(summary = "로그인 요청", description = "사용자의 이메일과 비밀번호를 확인하고 AccessToken, RefreshToken 을 발급합니다.")
    public CommonResponse login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {

        return CommonResponse.success(authService.login(requestDto, response), "로그인 성공!");

    }

    // 로그아웃
    @PostMapping("/api/auth/logout")
    @Operation(summary = "로그아웃 요청", description = "사용자의 RefreshToken 을 제거합니다.")
    public CommonResponse logout(@AuthenticationPrincipal UserPrincipal principal,HttpServletRequest request, HttpServletResponse response) {

        Long userId = principal.getUserId();

        authService.logout(userId, request, response);
        return CommonResponse.success(null, "로그아웃 성공!");

    }

    // 권한 변경
    @PatchMapping("/api/auth/{id}")
    @Operation(summary = "(관리자 전용) 권한 변경", description = "사용자의 권한을 변경합니다.")
    @TrackUserActionAnnotation("권한 변경")
    public CommonResponse changeRole(@Valid @RequestBody RoleRequestDto request, @PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {

        return CommonResponse.success(authService.changeRole(request, id, principal), "권한 변경 성공!");

    }

    // 할인 타입 변경
    @PatchMapping("/api/auth/discount/{id}")
    @Operation(summary = "(관리자 전용) 할인 타입 변경", description = "사용자의 할인 유형을 변경합니다.")
    public CommonResponse changeDiscountType(
            @Valid @RequestBody DiscountTypeRequestDto request,
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
            ) {

        return CommonResponse.success(authService.changeDiscountType(request, id, principal), "할인 타입 변경 성공!");

    }

    // Refresh Token 을 사용한 Access Token 재발급
    @PostMapping("/api/auth/refresh")
    @Operation(summary = "Access Token 재발급", description = "Refresh Token 으로 Access Token 을 재발급 합니다.")
    public CommonResponse refresh(@CookieValue("refreshToken") String refreshToken,
                               @AuthenticationPrincipal UserPrincipal principal,
                               HttpServletRequest request) {

        return CommonResponse.success(authService.refresh(refreshToken, principal, request), "재발급 성공!");

    }

}
