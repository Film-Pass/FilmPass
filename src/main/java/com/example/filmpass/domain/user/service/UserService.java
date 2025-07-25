package com.example.filmpass.domain.user.service;

import com.example.filmpass.domain.user.dto.UserDeleteDto;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
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

}
