package com.example.filmpass.domain.auth.service;

import com.example.filmpass.domain.auth.dto.AuthData;
import com.example.filmpass.domain.auth.dto.SignUpRequestDto;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.common.ApiResponse;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입 로직
    public ApiResponse<AuthData> signUp(SignUpRequestDto requestDto) {

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

        return ApiResponse.success(data, "회원가입 성공!");

    }

}
