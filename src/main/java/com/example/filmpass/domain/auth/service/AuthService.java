package com.example.filmpass.domain.auth.service;

import com.example.filmpass.domain.auth.dto.*;
import com.example.filmpass.domain.auth.entity.RefreshToken;
import com.example.filmpass.domain.auth.repository.RefreshTokenRepository;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.config.JwtUtil;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    // 회원가입 로직
    @Transactional
    public AuthData signUp(SignUpRequestDto requestDto) {

        // 저장할 값 꺼내기
        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String rawPassword = requestDto.getPassword();
        String nickname = requestDto.getNickname();

        // 중복 이메일 체크
        if(userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        // 중복 닉네임 체크
        if(userRepository.existsByNickname(nickname)) {
            throw new CustomException((ErrorCode.NICKNAME_ALREADY_EXIST));
        }

        String password = passwordEncoder.encode(rawPassword);

        User user = new User(email,password, nickname, name);

        userRepository.save(user);

        AuthData data = new AuthData(user.getEmail(), user.getNickname());

        return data;

    }

    // 로그인 로직
    @Transactional
    public String login(LoginRequestDto requestDto, HttpServletResponse response) {

        // 값 꺼내기
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 유저 조회
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        String encodedPassword = user.getPassword();

        // 비밀번호 검증
        boolean isMatch = passwordEncoder.matches(password, encodedPassword);

        if(!isMatch) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 탈퇴한 회원인지 검증
        if(user.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.DELETED_USER);
        }

        // RefreshToken 있으면 삭제
        if(refreshTokenRepository.existsByUser_Id(user.getId())) {
            refreshTokenRepository.deleteByUser_Id(user.getId());
        }

        // 토큰 생성
        String accessToken = jwtUtil.createToken(user.getId(), user.getNickname(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getId(), user.getNickname(), user.getRole());

        // RefreshToken 객체 생성
        LocalDateTime expiredAt = jwtUtil.extractExpiredAt(refreshToken);
        RefreshToken refresh = new RefreshToken(refreshToken, LocalDateTime.now(), expiredAt, user);

        // RefreshToken 저장
        refreshTokenRepository.save(refresh);

        // Cookie 에 RefreshToken 세팅
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // JavaScript 접근 불가 -> XSS 방지
        cookie.setSecure(false); // HTTPS 에서만 전송할건지 여부
        cookie.setPath("/"); // 모든 경로에 대해 쿠키 포함
        cookie.setMaxAge(7 * 24 * 60 * 60); // 만료기간 7일

        response.addCookie(cookie);

        return accessToken;

    }


    // 로그아웃 로직
    @Transactional
    public void logout(Long userId, HttpServletResponse response) {

        refreshTokenRepository.deleteByUser_Id(userId);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }


    // 유저 권한 변경 로직
    @Transactional
    public String changeRole(RoleRequestDto request, Long id, UserPrincipal principal) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // ADMIN 만 권한변경 가능하도록 검증
        if(principal.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }

        // 입력한 권한이 현재 권한과 같은 Role 인지 검증
        if(user.getRole() == request.getUserRole()) {
            throw new CustomException(ErrorCode.CANNOT_CHANGE_SAME_ROLE);
        }


        user.setRole(request.getUserRole());

        userRepository.save(user);

        return user.getRole().name();

    }


    // 할인 타입 변경 로직
    @Transactional
    public String changeDiscountType(DiscountTypeRequestDto request, Long id, UserPrincipal principal) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // ADMIN 만 할인 타입을 변경 가능하도록 검증
        if(principal.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }

        // 입력한 타입이 현재 타입과 같은 Type 인지 검증
        if(user.getDiscountType() == request.getDiscountType()) {
            throw new CustomException(ErrorCode.CANNOT_CHANGE_SAME_ROLE);
        }

        user.setDiscountType(request.getDiscountType());

        userRepository.save(user);

        return user.getDiscountType().name();

    }

}
