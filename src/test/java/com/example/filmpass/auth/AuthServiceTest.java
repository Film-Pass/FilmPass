package com.example.filmpass.auth;

import com.example.filmpass.domain.auth.dto.*;
import com.example.filmpass.domain.auth.repository.RefreshTokenRepository;
import com.example.filmpass.domain.auth.service.AuthService;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.DiscountType;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.config.JwtUtil;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletResponse response;

    @Captor
    private ArgumentCaptor<Cookie> cookieCaptor;


    // 회원가입 테스트
    @Test
    public void 회원가입시_AuthData에_값이_주입된다() {

        // given - requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String nickname = "사용자 닉네임";
        SignUpRequestDto requestDto = new SignUpRequestDto(name, email, password, nickname);

        when(userRepository.existsByEmail("testexample123@domain.com")).thenReturn(false);
        when(userRepository.existsByNickname("사용자 닉네임")).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // when
        AuthData authData = authService.signUp(requestDto);

        // then
        assertThat(authData.getEmail()).isEqualTo("testexample123@domain.com");
        assertThat(authData.getNickname()).isEqualTo("사용자 닉네임");

    }

    @Test
    public void 회원가입시_이메일이_중복되면_CustomException을_던진다() {

        // given - requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String nickname = "사용자 닉네임";
        SignUpRequestDto requestDto = new SignUpRequestDto(name, email, password, nickname);

        when(userRepository.existsByEmail("testexample123@domain.com")).thenReturn(true);

        // when, then
        assertThrows(CustomException.class, () -> authService.signUp(requestDto));

    }

    @Test
    public void 로그인시_accessToken과_refreshToken이_만들어지고_쿠키가_세팅된다() {

        // given - requestDto, User, repository 행위, passwordEncoder 행위, JwtUtil 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        when(userRepository.findByEmail("testexample123@domain.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(refreshTokenRepository.existsByUser_Id(1L)).thenReturn(false);
        when(jwtUtil.createToken(1L, "사용자 닉네임", UserRole.GUEST)).thenReturn("accessToken");
        when(jwtUtil.createRefreshToken(1L, "사용자 닉네임", UserRole.GUEST)).thenReturn("refreshTokenClaim");
        when(jwtUtil.extractExpiredAt("refreshTokenClaim")).thenReturn(LocalDateTime.now().plusDays(7));

        // when
        String accessToken = authService.login(requestDto, response);

        //then
        assertThat(accessToken).isEqualTo("accessToken");

        verify(response).addCookie(cookieCaptor.capture());  // response 의 쿠키를 가로채서 검증
        Cookie refreshCookie = cookieCaptor.getValue(); // 가로챈 쿠키를 꺼냄.

        assertThat(refreshCookie.getName()).isEqualTo("refreshToken");
        assertThat(refreshCookie.getValue()).isEqualTo("refreshTokenClaim");
        assertThat(refreshCookie.isHttpOnly()).isEqualTo(true);
        assertThat(refreshCookie.getMaxAge()).isEqualTo(7 * 24 * 60 *60);
        assertThat(refreshCookie.getPath()).isEqualTo("/");


    }

    @Test
    public void 로그아웃시_쿠키가_없어진다() {

        // given - 세팅값 X

        // when
        authService.logout(1L, response);

        // then
        verify(response).addCookie(cookieCaptor.capture());
        Cookie refreshCookie = cookieCaptor.getValue();

        assertThat(refreshCookie.getValue()).isNull();
        assertThat(refreshCookie.isHttpOnly()).isEqualTo(true);
        assertThat(refreshCookie.getMaxAge()).isEqualTo(0);
        assertThat(refreshCookie.getPath()).isEqualTo("/");

    }

    @Test
    public void 관리자가_다른유저의_권한을_GUEST에서_ADMIN으로_변경한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        UserPrincipal principal = new UserPrincipal(2L, "관리자 계정 닉네임", UserRole.ADMIN);

        RoleRequestDto requestDto = new RoleRequestDto(UserRole.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        String changedRoleName = authService.changeRole(requestDto, 1L, principal);

        // then
        assertThat(changedRoleName).isEqualTo(UserRole.ADMIN.name());

    }

    @Test
    public void 관리자가_다른유저의_할인타입을_NORMAL에서_PATRIOT으로_변경한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        UserPrincipal principal = new UserPrincipal(2L, "관리자 계정 닉네임", UserRole.ADMIN);

        DiscountTypeRequestDto requestDto = new DiscountTypeRequestDto(DiscountType.PATRIOT);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        String changedType = authService.changeDiscountType(requestDto, 1L, principal);

        // then
        assertThat(changedType).isEqualTo(DiscountType.PATRIOT.name());

    }

}
