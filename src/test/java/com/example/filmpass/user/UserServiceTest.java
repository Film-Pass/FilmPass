package com.example.filmpass.user;

import com.example.filmpass.domain.user.dto.PasswordRequestDto;
import com.example.filmpass.domain.user.dto.UserDetailsResponseDto;
import com.example.filmpass.domain.user.dto.UserInfoChangeRequestDto;
import com.example.filmpass.domain.user.dto.UserInfoResponseDto;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.domain.user.service.UserService;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // 회원탈퇴 - 성공
    @Test
    public void 회원탈퇴시_삭제일이_기록된다() {

        // given - 삭제할 User, repository 행위, passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encoded1234")).thenReturn(true);


        // when
        userService.deleteUser(1L, password);

        // then
        assertThat(user.getDeletedAt()).isNotNull();
        verify(userRepository).save(user);

    }

    // 회원탈퇴 - 존재하지 않는 유저를 조회
    @Test
    public void 회원탈퇴에서_존재하지않는_유저를_조회시_CustomException이_발생한다() {

        // given - repository 행위
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.deleteUser(1L, "Password123!"));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

    // 회원탈퇴 - 틀린 비밀번호를 입력
    @Test
    public void 회원탈퇴에서_잘못된_비밀번호를_입력시_CustomException이_발생한다() {

        // given - 삭제할 User, repository 행위 passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encoded1234")).thenReturn(false);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.deleteUser(1L, password));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_MISMATCH);

    }

    // 회원탈퇴 - 이미 탈퇴한 회원으로 요청
    @Test
    public void 탈퇴한_회원으로_회원탈퇴_요청시_CustomException이_발생한다() {

        // given - 삭제할 User, repository 행위 passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setDeletedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encoded1234")).thenReturn(true);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.deleteUser(1L, password));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DELETED_USER);

    }

    // 유저 목록 조회 - 성공
    @Test
    public void 유저목록_조회시_페이지크기와_페이지번호가_정상적으로_반환된다() {

        // given - pageable, List<User>, Page<User>, UserPrincipal, repository 행위
        int page = 3;
        int size = 2;

        Pageable pageable = PageRequest.of(page - 1, size);


        User user5 = new User("a5@a.a", "encoded5", "nickname5", "name5");
        user5.setId(5L);
        List<User> userList = List.of(user5);

        Page<User> userPage = new PageImpl<>(userList, pageable, 5);

        UserPrincipal principal = new UserPrincipal(6L, "관리자 계정 닉네임", UserRole.ADMIN);

        when(userRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc(pageable)).thenReturn(userPage);

        // when
        Page<UserInfoResponseDto> response = userService.getUsers(page, size, principal);

        // then
        assertThat(response.getNumber()).isEqualTo(2);
        assertThat(response.getSize()).isEqualTo(2);
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(5);
        verify(userRepository).findAllByDeletedAtIsNullOrderByCreatedAtDesc(pageable);

    }

    // 유저 목록 조회 - 관리자 권한이 없는 요청
    @Test
    public void 유저목록_조회시_관리자_권한이_없으면_CustomException이_발생한다() {

        // given - UserPrincipal
        UserPrincipal principal = new UserPrincipal(7L, "일반 계정 닉네임", UserRole.GUEST);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.getUsers(3, 2, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_ADMIN);

    }

    // 유저 목록 조회 - 잘못된 페이지 크기로 요청
    @Test
    public void 유저목록_조회시_페이지_크기가_정상적이지_않으면_CustomException이_발생한다() {

        // given - UserPrincipal
        UserPrincipal principal = new UserPrincipal(6L, "관리자 계정 닉네임", UserRole.ADMIN);

        // when, then
        CustomException e1 = assertThrows(CustomException.class, () -> userService.getUsers(3, 1000, principal));
        CustomException e2 = assertThrows(CustomException.class, () -> userService.getUsers(3, -5, principal));
        assertThat(e1.getErrorCode()).isEqualTo(ErrorCode.INVALID_PAGE_SIZE);
        assertThat(e2.getErrorCode()).isEqualTo(ErrorCode.INVALID_PAGE_SIZE);

    }

    // 유저 목록 조회 - 페이지가 1 미만인 요청
    @Test
    public void 유저목록_조회시_페이지가_정상적이지_않으면_CustomException이_발생한다() {

        // given - UserPrincipal
        UserPrincipal principal = new UserPrincipal(6L, "관리자 계정 닉네임", UserRole.ADMIN);

        // when, then
        CustomException e1 = assertThrows(CustomException.class, () -> userService.getUsers(0, 2, principal));
        CustomException e2 = assertThrows(CustomException.class, () -> userService.getUsers(-10, 2, principal));
        assertThat(e1.getErrorCode()).isEqualTo(ErrorCode.INVALID_PAGE_NUMBER);
        assertThat(e2.getErrorCode()).isEqualTo(ErrorCode.INVALID_PAGE_NUMBER);

    }

    // 유저 목록 조회 - 2 이상의 페이지가 빈 페이지인 요청
    @Test
    public void 유저목록_조회시_1페이지를_제외한_나머지_페이지가_비어있으면_CustomException이_발생한다() {

        // given - pageable, 빈 페이지, UserPrincipal, repository 행위
        int page = 3;
        int size = 2;

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<User> emptyPage = new PageImpl<>(java.util.Collections.emptyList(), pageable, 5);

        UserPrincipal principal = new UserPrincipal(6L, "관리자 계정 닉네임", UserRole.ADMIN);

        when(userRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc(pageable)).thenReturn(emptyPage);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.getUsers(page, size, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.EMPTY_PAGE);
    }

    // 유저 단건 조회 - 성공
    @Test
    public void 유저단건_조회시_유저상세정보가_정상적으로_반환된다() {

        // given - 조회될 유저, UserPrincipal, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        UserPrincipal principal = new UserPrincipal(1L, "관리자 계정 닉네임", UserRole.ADMIN);

        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(user));

        // when
        UserDetailsResponseDto response = userService.getUser(1L, principal);

        // then
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUserRole()).isEqualTo(UserRole.GUEST.name());
        assertThat(response.getName()).isEqualTo("사용자명");
        assertThat(response.getNickname()).isEqualTo("사용자 닉네임");
        assertThat(response.getDeletedAt()).isNull();
        assertThat(response.getEmail()).isEqualTo("testexample123@domain.com");
        verify(userRepository).findByIdAndDeletedAtIsNull(1L);

    }

    // 유저 단건 조회 - 관리자 권한이 없는 요청
    @Test
    public void 유저단건_조회시_관리자가_아니면_CustomException이_발생한다() {

        // given - UserPrincipal
        UserPrincipal principal = new UserPrincipal(1L, "일반 계정 닉네임", UserRole.GUEST);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.getUser(1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NOT_ADMIN);

    }

    // 유저 단건 조회 - 존재하지 않는 유저를 조회
    @Test
    public void 유저단건_조회시_없는_유저를_조회하면_CustomException이_발생한다() {

        // given - UserPrincipal, repository 행위
        UserPrincipal principal = new UserPrincipal(1L, "관리자 계정 닉네임", UserRole.ADMIN);

        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.getUser(1L, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

    // 내 정보 조회 - 성공
    @Test
    public void 내정보_조회시_정보가_정상적으로_반환된다() {

        // given - 조회될 User, requestDto, userPrincipal, repository 행위, passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        PasswordRequestDto requestDto = new PasswordRequestDto("Password123!");

        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encoded1234")).thenReturn(true);

        // when
        UserInfoResponseDto response = userService.getMyProfile(requestDto, principal);

        // then
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("사용자명");
        assertThat(response.getNickname()).isEqualTo("사용자 닉네임");
        assertThat(response.getEmail()).isEqualTo("testexample123@domain.com");
        verify(userRepository).findById(1L);

    }

    // 내 정보 조회 - 존재하지 않는 유저를 조회
    @Test
    public void 내정보_조회시_존재하지_않는_유저를_조회하면_CustomException이_발생한다() {

        // given - requestDto, UserPrincipal
        PasswordRequestDto requestDto = new PasswordRequestDto("Password123!");

        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.getMyProfile(requestDto, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

    // 내 정보 조회 - 잘못된 비밀번호로 조회 요청
    @Test
    public void 내정보_조회시_틀린_비밀번호로_요청하면_CustomException이_발생한다() {

        // given - User, requestDto, UserPrincipal, reposiroty 행위, passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        PasswordRequestDto requestDto = new PasswordRequestDto("Password123!");

        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encoded1234")).thenReturn(false);

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.getMyProfile(requestDto, principal));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_MISMATCH);

    }

    // 내 정보 수정 - 성공
    @Test
    public void 내정보를_수정시_수정한_정보가_정상적으로_반환된다() {

        // given - 정보수정할 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        UserInfoChangeRequestDto requestDto = new UserInfoChangeRequestDto(
                "변경된 이름",
                "changed123@domain.com",
                "Changed123!",
                "변경된 닉네임");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        UserInfoResponseDto response = userService.changeUserInfo(requestDto, principal, 1L);

        // then
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("변경된 이름");
        assertThat(response.getNickname()).isEqualTo("변경된 닉네임");
        assertThat(response.getEmail()).isEqualTo("changed123@domain.com");
        verify(userRepository).save(user);

    }

    // 내 정보 수정 - 존재하지 않는 유저를 조회
    @Test
    public void 내정보_수정시_존재하지_않는_유저를_조회하면_CustomException이_발생한다() {

        // given - UserPrincipal, requestDto, repository 행위
        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        UserInfoChangeRequestDto requestDto = new UserInfoChangeRequestDto(
                "변경된 이름",
                "changed123@domain.com",
                "Changed123!",
                "변경된 닉네임");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.changeUserInfo(requestDto, principal, 1L));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

    // 내 정보 수정 - 타인의 정보를 수정 요청
    @Test
    public void 내정보_수정시_타인의_정보를_수정요청하면_CustomException이_발생한다() {

        // given - 정보수정할 User, UserPrincipal, requestDto, repository 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(2L);

        UserPrincipal principal = new UserPrincipal(1L, "사용자 닉네임", UserRole.GUEST);

        UserInfoChangeRequestDto requestDto = new UserInfoChangeRequestDto(
                "변경된 이름",
                "changed123@domain.com",
                "Changed123!",
                "변경된 닉네임");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.changeUserInfo(requestDto, principal, 2L));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.CHANGE_BLOCKED);

    }

    // 포인트 적립 - 성공
    @Test
    public void 포인트_적립시_정상적으로_적립된다() {

        // given - 적립될 point, 적립시킬 User, repository 행위
        int point = 1001;

        String name = "사용자명";
        String email = "testexample123@domain.com";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        userService.addPoint(1L, point);

        // then
        assertThat(user.getPoint()).isEqualTo(1001);
        verify(userRepository).save(user);

    }

    // 포인트 적립 - 존재하지 않는 유저를 조회
    @Test
    public void 포인트를_존재하지않는_유저에_적립하려하면_CustomException이_발생한다() {

        // given - repository 행위
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        CustomException e = assertThrows(CustomException.class, () -> userService.addPoint(1L, 1001));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

    }

}
