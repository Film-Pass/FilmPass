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
import com.example.filmpass.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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


    // 회원가입 - 성공
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

    // 회원가입 - 이메일 중복
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
        CustomException e = assertThrows(CustomException.class, () -> authService.signUp(requestDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXIST);

    }

    // 회원가입 - 닉네임 중복
    @Test
    public void 회원가입시_닉네임이_중복되면_CustomeException이_발생한다() {

        // given - requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String nickname = "사용자 닉네임";
        SignUpRequestDto requestDto = new SignUpRequestDto(name, email, password, nickname);

        when(userRepository.existsByNickname("사용자 닉네임")).thenReturn(true);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.signUp(requestDto));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NICKNAME_ALREADY_EXIST);

    }

    // 로그인 - 성공
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
        assertThat(refreshCookie.getMaxAge()).isEqualTo(7 * 24 * 60 * 60);
        assertThat(refreshCookie.getPath()).isEqualTo("/");


    }

    // 로그인 - 존재하지 않는 유저 조회
    @Test
    public void 존재하지_않는_이메일로_로그인시_CustomException이_발생한다() {

        // given - repository 행위
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class,
                () -> authService.login(new LoginRequestDto("e", "p"), new MockHttpServletResponse()));

        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    // 로그인 - 틀린 비밀번호로 로그인
    @Test
    public void 잘못된_비밀번호로_로그인시_CustomException이_발생한다() {

        // given - User, requestDto, repository 행위, passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        User user = new User(email, encodedPassword, nickname, name);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", encodedPassword)).thenReturn(false);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.login(requestDto, new MockHttpServletResponse()));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_MISMATCH);

    }

    // 로그인 - 탈퇴한 계정으로 로그인
    @Test
    public void 탈퇴한_계정으로_로그인시_CustomException이_발생한다() {

        // given - User, requestDto, repository 행위, passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        User user = new User(email, encodedPassword, nickname, name);
        user.setDeletedAt(LocalDateTime.now());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", encodedPassword)).thenReturn(true);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.login(requestDto, new MockHttpServletResponse()));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DELETED_USER);
    }

    // 로그아웃 - 성공
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

    // 권한 변경 - 성공
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

    // 권한 변경 - 존재하지 않는 유저를 조회
    @Test
    public void 권한변경에서_존재하지_않는_유저를_조회시_CustomException이_발생한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        UserPrincipal principal = new UserPrincipal(2L, "관리자 계정 닉네임", UserRole.ADMIN);

        RoleRequestDto requestDto = new RoleRequestDto(UserRole.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.changeRole(requestDto, 1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

    // 권한 변경 - 관리자 권한이 없는 요청
    @Test
    public void 권한변경에서_관리자_권한이_없는_요청이_들어오면_CustomException이_발생한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        UserPrincipal principal = new UserPrincipal(3L, "일반 계정 닉네임", UserRole.GUEST);

        RoleRequestDto requestDto = new RoleRequestDto(UserRole.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.changeRole(requestDto, 1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_ADMIN);

    }

    // 권한 변경 - 같은 권한으로 변경 요청
    @Test
    public void 권한변경에서_같은_권한으로_변경을_요청하면_CustomException이_발생한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        UserPrincipal principal = new UserPrincipal(2L, "관리자 계정 닉네임", UserRole.ADMIN);

        RoleRequestDto requestDto = new RoleRequestDto(UserRole.GUEST);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.changeRole(requestDto, 1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.CANNOT_CHANGE_SAME_ROLE);
    }

    // 할인타입 변경 - 성공
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

    // 할인타입 변경 - 존재하지 않는 유저를 조회
    @Test
    public void 할인타입_변경에서_존재하지_않는_유저를_조회시_CustomException이_발생한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        UserPrincipal principal = new UserPrincipal(2L, "관리자 계정 닉네임", UserRole.ADMIN);

        DiscountTypeRequestDto requestDto = new DiscountTypeRequestDto(DiscountType.PATRIOT);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.changeDiscountType(requestDto, 1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

    // 할인타입 변경 - 관리자 권한이 없는 요청
    @Test
    public void 할인타입_변경에서_관리자_권한이_없는_요청이_들어오면_CustomException이_발생한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        UserPrincipal principal = new UserPrincipal(3L, "일반 계정 닉네임", UserRole.GUEST);


        DiscountTypeRequestDto requestDto = new DiscountTypeRequestDto(DiscountType.PATRIOT);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.changeDiscountType(requestDto, 1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_ADMIN);
    }

    // 권한 변경 - 같은 권한으로 변경 요청
    @Test
    public void 할인타입_변경에서_같은_권한으로_변경을_요청하면_CustomException이_발생한다() {

        // given - 권한변경될 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        UserPrincipal principal = new UserPrincipal(2L, "관리자 계정 닉네임", UserRole.ADMIN);


        DiscountTypeRequestDto requestDto = new DiscountTypeRequestDto(DiscountType.NORMAL);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> authService.changeDiscountType(requestDto, 1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.CANNOT_CHANGE_SAME_ROLE);

    }

}
