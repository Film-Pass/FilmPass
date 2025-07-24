package com.example.filmpass.domain.user.service;

import com.example.filmpass.domain.user.dto.PageResponseDto;
import com.example.filmpass.domain.user.dto.UserDeleteDto;
import com.example.filmpass.domain.user.dto.UserDetailsResponseDto;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 탈퇴 로직
    public ApiResponse<?> deleteUser(Long id, UserDeleteDto requestDto) {

        // 유저 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 탈퇴한 회원인지 검증
        if(user.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.DELETED_USER);
        }

        // 소프트 삭제 진행
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return ApiResponse.success(null, "회원 탈퇴가 완료되었습니다");

    }


    // 유저 목록 조회
    public ApiResponse<Page<PageResponseDto>> getUsers(int page, int size, UserPrincipal principal) {

        // 권한 확인
        if(principal.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }

        Pageable pageable = PageRequest.of(page-1, size);

        Page<User> users = userRepository.findAllByOrderByCreatedAtDesc(pageable);

        Page<PageResponseDto> response = users.map(User::pageToDto);

        return ApiResponse.success(response, "유저목록 조회성공!");
    }


    // 유저 단건 조회
    public ApiResponse<UserDetailsResponseDto> getUser(Long id, UserPrincipal principal) {

        // 권한 확인
        if(principal.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }


            User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserDetailsResponseDto response = new UserDetailsResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNickname(),
                user.getDeletedAt(),
                user.getRole().name()
        );

        return ApiResponse.success(response,"유저정보 조회성공!");

    }

}
