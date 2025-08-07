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


    @Test
    public void 회원탈퇴시_비밀번호가_틀리면_CustomException을_던진다() {

        // given - 삭제할 User, repository 행위, passwordEncoder 행위
        String name = "사용자명";
        String email = "testexample123@domain.com";
        String password = "Password123!";
        String encodedPassword = "encoded1234";
        String nickname = "사용자 닉네임";

        User user = new User(email, encodedPassword, nickname, name);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encoded1234")).thenReturn(false);

        // when, then
        assertThrows(CustomException.class, () -> userService.deleteUser(1L, password));


    }


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


    @Test
    public void 내정보를_수정시_수정한_정보가_정상적으로_반환된다() {

        // given - 정보수정할 User, UserPrincipal, requestDto, repository 행위, passwordEncoder 행위
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
        when(passwordEncoder.matches("Changed123!", "encoded1234")).thenReturn(false);

        // when
        UserInfoResponseDto response = userService.changeUserInfo(requestDto, principal, 1L);

        // then
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("변경된 이름");
        assertThat(response.getNickname()).isEqualTo("변경된 닉네임");
        assertThat(response.getEmail()).isEqualTo("changed123@domain.com");
        verify(userRepository).save(user);

    }


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

}
